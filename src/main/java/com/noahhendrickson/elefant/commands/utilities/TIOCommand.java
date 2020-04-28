package com.noahhendrickson.elefant.commands.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import org.json.JSONObject;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;

import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TIOCommand implements ICommand {
	
	private static final String LANGUAGE_URL = "https://tio.run/static/3fbdee7a34cd8d340fe2dbd19acd2391-languages.json";
	private static final String RUN_URL = "https://tio.run/cgi-bin/static/fb67788fd3d1ebf92e66b295525335af-run";
	
	private static final OkHttpClient CLIENT = new OkHttpClient();
	
	private static Map<String, String> languages, lookup;
	
	static {
		languages = new HashMap<>();
		lookup = new HashMap<>();
		try {
			Request request = new Request.Builder().get().url(LANGUAGE_URL).build();
			Response response = CLIENT.newCall(request).execute();
			
			// So, I can't use GSON without creating a class to hold the data
			// That's a bit overkill as we only need 2 values (key and the "name" value)
			JSONObject json = new JSONObject(response.body().string());
			
			for(String key : json.keySet()) {
				languages.put(key, json.getJSONObject(key).getString("name"));
				lookup.put(json.getJSONObject(key).getString("name"), key);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(CommandBundle bundle) {
		if(!bundle.hasArgs()) {
			bundle.sendUsageMessage();
			return;
		}
		String codeblockStart = bundle.getArgAt(0);
		String language = codeblockStart.substring(3);
		if(language.contains("\n")) {
			language = language.substring(0, language.indexOf("\n"));
		}
		String code = String.join(" ", bundle.getArgs());
		code = code.substring(3 + language.length(), code.length()-3);
		
		final String lang = language;
		if(!languages.keySet().stream().anyMatch(name -> name.equalsIgnoreCase(lang))) {
			language = lookup.keySet()
							 .stream()
							 .filter(name -> name.toLowerCase().contains(lang.toLowerCase()))
							 .map(lookup::get)
							 .sorted()
							 .findFirst()
							 .orElse(null);
		}
		if(language == null) {
			bundle.sendMessage(String.format("Unknown language: `%s`", MarkdownSanitizer.sanitize(lang, MarkdownSanitizer.SanitizationStrategy.REMOVE)));
			return;
		}
		String pretty = languages.get(language);
		byte[] body = this.encodeBody(language, code);
		bundle.sendMessage("Language: `" + pretty + "`");
		
		bundle.getChannel().sendMessage("Evaluating...").queue(m -> {
			try {
				Request request = new Request.Builder()
											 .header("Accept-Encoding", "text/html")
											 .post(RequestBody.create(MediaType.get("application/octet-stream"), body))
											 .url(RUN_URL)
											 .build();
				Response response = CLIENT.newCall(request).execute();
				byte[] gzip = response.body().bytes();
				byte[] output = new byte[1<<16];
				
				Inflater inflater = new Inflater(true);
				inflater.setInput(Arrays.copyOfRange(gzip, 10, gzip.length));
				output = Arrays.copyOfRange(output, 0, inflater.inflate(output));
				inflater.end();
				
				String result = new String(output);
				String seperator = result.substring(0, 16);
				
				String[] results = result.split(Pattern.quote(seperator));
				
				m.editMessage("Output:\n```" + MarkdownSanitizer.sanitize(String.join("\n", results), MarkdownSanitizer.SanitizationStrategy.ESCAPE)+"```").queue();
			}
			catch(Exception e) {
				bundle.sendMessage(e.toString());
				e.printStackTrace();
			}
		});
	}
	
	private byte[] encodeBody(String language, String code) {
		ByteBuffer buff = ByteBuffer.allocate(5+language.length()+code.length()+27+1+12+String.valueOf(code.length()).length());
		buff.put("Vlang\0\u0031\0".getBytes());
		buff.put(language.getBytes());
		buff.put("\0VTIO_OPTIONS\00\0F.code.tio\0".getBytes());
		buff.put(String.valueOf(code.length()).getBytes());
		buff.put(("\0"+code).getBytes());
		buff.put("\00\0Vargs\00\0R".getBytes());
		
		byte[] body = buff.array();
		
		Deflater compresser = new Deflater(9, true);
		compresser.setInput(body);
		compresser.finish();
		
		byte[] output = new byte[1<<16];
		int length = compresser.deflate(output);
		
		compresser.end();
		
		return Arrays.copyOfRange(output, 0, length);
	}
	@SuppressWarnings("unused")
	private byte[] decodeResponse(byte[] response) throws DataFormatException, IOException {
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(response));
		
		byte[] output = new byte[1<<16];
		int written = 0;
		int end;
		
		byte[] buff = new byte[1024];
		while((end = gzip.read(buff)) != -1) {
			System.arraycopy(buff, 0, output, written, end);
			written += end;
		}
		return Arrays.copyOfRange(output, 0, written);
	}

	@Override
	public String getCommand() {
		return "run";
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList("tio", "evaluate");
	}

	@Override
	public String getDescription() {
		return "Runs provided code using the Try it Online site";
	}
	
	@Override
	public String getUsage() {
		return "\n\\`\\`\\`language\n{code}\n\\`\\`\\`";
	}
	
	@SuppressWarnings("unused")
	private String convertToString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		boolean wasUnprintable = false;
		String now = null;
		for(int b : bytes) {
			if(b < 0) {
				b&=0xff;
			}
			if((now = new String(new byte[]{(byte)b})).matches("\\p{C}")) {
				if(!wasUnprintable && sb.length() > 0) {
					sb.append(' ');
				}
				sb.append(String.format("0x%02x ", b));
				wasUnprintable = true;
			}
			else {
				if(wasUnprintable) {
					if(now.matches("\\p{P}")) {
						sb.deleteCharAt(sb.length()-1);
					}
					wasUnprintable = false;
				}
				sb.append((char)b);
			}
		}
		return sb.toString();
	}
}
