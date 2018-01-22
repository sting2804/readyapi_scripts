package com.twentytwoskills.certificates;

//import com.eviware.soapui.support.UISupport
import java.text.MessageFormat

class CertificateSource {

    private Properties properties

    CertificateSource() {
        properties = new Properties();
        def scriptFile = getClass().protectionDomain.codeSource.location.path
        scriptFile = scriptFile.substring(0, scriptFile.indexOf('CertificateSource.groovy'))
        //UISupport.showInfoMessage("ScriptPath: $scriptFile")
        properties.load(new FileInputStream(scriptFile+'certificate.properties'))
    }

    protected String getMessage(String code, Object[] args) {
        if (code == null) {
            return null;
        }

        Object[] argsToUse

        MessageFormat messageFormat = resolveCode(code);
        if ((args as List).isEmpty()) {
            if (messageFormat != null) {
                synchronized (messageFormat) {
                    return messageFormat.format(new Object[0]);
                }
            }
        } else {
            argsToUse = resolveArguments(args);
            if (messageFormat != null) {
                synchronized (messageFormat) {
                    return messageFormat.format(argsToUse);
                }
            }
        }
        return null
    }

    MessageFormat resolveCode(String code) {

        String msg = properties.getProperty(code);
        if (msg != null) {
            return new MessageFormat((msg != null ? msg : ""));
        }
        return null

    }

    protected Object[] resolveArguments(Object[] args) {
        if (args == null) {
            return new Object[0];
        }
        List<Object> resolvedArgs = new ArrayList<Object>(args.length);
        for (Object arg : args) {
            resolvedArgs.add(arg);
        }
        return resolvedArgs.toArray(new Object[resolvedArgs.size()]);
    }

}
