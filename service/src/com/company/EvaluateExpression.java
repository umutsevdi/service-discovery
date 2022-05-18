package com.company;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class EvaluateExpression implements Executor {
    public EvaluateExpression() {
    }

    @Override
    public String execute(String args) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("javascript");
        try {
            return (String) engine.eval(args);
        } catch (ScriptException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }
}
