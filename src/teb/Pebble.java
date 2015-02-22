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
import com.mitchellbosecke.pebble.template.PebbleTemplate;

public class Pebble extends _BenchBase {

    PebbleEngine engine;

    private String templateName = "templates/stocks.pebble.html";

    public Pebble() {
        engine = new PebbleEngine();
        engine.setStrictVariables(true);
    }

    @Override
    public void execute(Writer w0, Writer w1, int ntimes, List<Stock> items) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("items", items);

        while (--ntimes >= 0) {
            PebbleTemplate template = engine.getTemplate(templateName);
            if (ntimes == 0) {
                template.evaluate(w1, params);
            } else {
                template.evaluate(w0, params);
            }
        }
    }

    @Override
    protected void execute(OutputStream o0, OutputStream o1, int ntimes, List<Stock> items) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("items", items);
        Writer w1 = new OutputStreamWriter(o1);
        Writer w0 = new OutputStreamWriter(o0);

        if (_BenchBase.bufferMode.get()) {
            w0 = new BufferedWriter(w0);
            w1 = new BufferedWriter(w1);
        }
        while (--ntimes >= 0) {
            PebbleTemplate template = engine.getTemplate(templateName);
            if (ntimes == 0) {
                template.evaluate(w1, params);
            } else {
                template.evaluate(w0, params);
            }
        }
    }

    @Override
    protected String execute(int ntimes, List<Stock> items) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("items", items);
        StringWriter writer = null;

        while (--ntimes >= 0) {
            PebbleTemplate template = engine.getTemplate(templateName);
            writer = new StringWriter();
            template.evaluate(writer, params);
        }

        return writer.toString();
    }

    public static void main(String[] args) {
        new Pebble().run();
    }

}
