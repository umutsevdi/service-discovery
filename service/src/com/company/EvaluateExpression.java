package com.company;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class EvaluateExpression implements Runnable{

    private static String expression;

    public EvaluateExpression(String expression) {
        this.expression = expression;
    }


    @Override
    public void run() {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try{
            System.out.println(engine.eval(expression));
        }catch (ScriptException ex){
            ex.printStackTrace();
        }
    }
}
