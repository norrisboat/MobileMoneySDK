package com.apps.sancorp.paywithmobilemoney.core;

import android.content.Context;
import android.util.Log;

import com.apps.sancorp.paywithmobilemoney.offline.PrefsManager;
import com.apps.sancorp.paywithmobilemoney.utils.Constants;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by sam_davies on 12/28/16.
 */

class MobileMoneyPayment {
    private final PrefsManager prefsManager;

    MobileMoneyPayment(Context context){
        prefsManager = new PrefsManager(context);
    }

    String postInvoice(String name, String description, int billPrompt, String amount, String number, String message) {
        String SOAP_ACTION = "urn:postInvoice";
        String OPERATION_NAME = "postInvoice";
        String WSDL_TARGET_NAMESPACE = "http://services.webclient.transflow.dialect.com.gh";
        String SOAP_ADDRESS = "http://68.169.57.66:8080/transflow_webclient/services/InvoicingService.InvoicingServiceHttpSoap11Endpoint/";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
        request.addProperty("name",name);
        request.addProperty("info",description);
        request.addProperty("amt",amount);
        request.addProperty("mobile",number);
        request.addProperty("mesg",message);
        request.addProperty("expiry","?");
        request.addProperty("billprompt",billPrompt);
        request.addProperty("thirdpartyID",prefsManager.getThirdPartyId());
        request.addProperty("username", prefsManager.getUsername());
        request.addProperty("password", prefsManager.getPassword());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;

        try {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);

            response = httpTransport.responseDump;
        } catch (Exception ex) {
            Log.e(Constants.LOG_TAG,"payment class "+ex.toString());
            ex.printStackTrace();
        }
        Log.v(Constants.LOG_TAG,"post invoice response "+response);
        return response;
    }

    String checkInvStatus(int invoiceNumber) {
        String SOAP_ACTION = "urn:checkInvStatus";
        String OPERATION_NAME = "checkInvStatus";
        String WSDL_TARGET_NAMESPACE = "http://services.webclient.transflow.dialect.com.gh";
        String SOAP_ADDRESS = "http://68.169.57.66:8080/transflow_webclient/services/InvoicingService.InvoicingServiceHttpSoap11Endpoint/";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
        request.addProperty("invoiceNo",invoiceNumber);
        request.addProperty("username", prefsManager.getUsername());
        request.addProperty("password", prefsManager.getPassword());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;

        try {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);

            response = httpTransport.responseDump;
        } catch (Exception ex) {
            Log.e(Constants.LOG_TAG,"stuatu class "+ex.toString());
            ex.printStackTrace();
        }
        Log.v("Constants.LOG_TAG","invoice "+response);

        return response;
    }

    public String sendSMS(String mobile, String message, String source) {
        String SOAP_ACTION = "urn:sendSMS";
        String OPERATION_NAME = "sendSMS";
        String WSDL_TARGET_NAMESPACE = "http://services.webclient.transflow.dialect.com.gh";
        String SOAP_ADDRESS = "http://68.169.57.64:8080/transflow_webclient/services/InvoicingService?wsdl";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

        request.addProperty("mobile", mobile);
        request.addProperty("mesg", message);
        request.addProperty("src", source);
        request.addProperty("username", prefsManager.getUsername());
        request.addProperty("password", prefsManager.getPassword());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;

        try {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);

            response = httpTransport.responseDump;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.v(Constants.LOG_TAG,"respons1 "+ex.toString());
        }
        return response;
    }
}