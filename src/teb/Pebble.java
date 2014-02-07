/*
 * $Copyright: copyright(c) 2007-2011 kuwata-lab.com all rights reserved. $
 * $License: Creative Commons Attribution (CC BY) $
 */
package teb;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teb.model.Stock;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

public class Pebble extends _BenchBase {

	PebbleEngine engine;
	private String templateName = "stocks.pebble.html";

	public Pebble() {
		engine = new PebbleEngine(new FileLoader());
		engine.getLoader().setPrefix("templates/");

	}

	@Override
	public void execute(Writer w0, Writer w1, int ntimes, List<Stock> items) throws Exception {

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("items", items);

		PebbleTemplate template = engine.compile(templateName);

		while (--ntimes >= 0) {
			if (ntimes == 0) {
				template.evaluate(w1, context);
				w1.close();
			} else {
				template.evaluate(w0, context);
			}
		}
	}

	@Override
	protected void execute(OutputStream o0, OutputStream o1, int ntimes, List<Stock> items) throws Exception {

		Writer w0 = new OutputStreamWriter(o0);
		Writer w1 = new OutputStreamWriter(o1);

		if (_BenchBase.bufferMode.get()) {
			w0 = new BufferedWriter(w0);
			w1 = new BufferedWriter(w1);
		}

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("items", items);

		PebbleTemplate template = engine.compile(templateName);
		while (--ntimes >= 0) {

			if (ntimes == 0) {
				template.evaluate(w1, context);
				w1.close();
			} else {
				template.evaluate(w0, context);
			}
		}
	}

	@Override
	protected String execute(int ntimes, List<Stock> items) throws Exception {

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("items", items);

		Writer w0 = new StringWriter();

		PebbleTemplate template = engine.compile(templateName);
		while (--ntimes >= 0) {
			template.evaluate(w0, context);
		}

		return w0.toString();
	}

	public static void main(String[] args) {
		new Pebble().run();
	}

}
