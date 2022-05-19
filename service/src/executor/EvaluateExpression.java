package executor;

import exception.InvalidRequestException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class EvaluateExpression implements Executor {
    public EvaluateExpression() {
    }

    @Override
    public String execute(String args) throws InvalidRequestException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("javascript");
        try {
            return (String) engine.eval(args);
        } catch (ScriptException ex) {
            ex.printStackTrace();
            throw new InvalidRequestException();
        }
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.CALCULATOR;
    }
}
