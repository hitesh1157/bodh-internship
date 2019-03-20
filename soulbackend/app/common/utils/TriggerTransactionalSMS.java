package common.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import play.Configuration;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;

import java.util.HashMap;
import java.util.Map;

public class TriggerTransactionalSMS {

    public static void send(String mobile, String message, WSClient wsClient,Configuration configuration) {
        WSRequest wsRequest = wsClient.url(configuration.getString("sms.url"))
                .setQueryParameter("method", "sendMessage")
                .setQueryParameter("userid", configuration.getString("sms.userId"))
                .setQueryParameter("password", configuration.getString("sms.password"))
                .setQueryParameter("v", configuration.getString("sms.version"))
                .setQueryParameter("msg_type", configuration.getString("sms.msg_type"))
                .setQueryParameter("auth_scheme", configuration.getString("sms.auth_scheme"))
                .setQueryParameter("msg", message)
                .setQueryParameter("send_to", mobile);
        wsRequest.execute(); //TODO : handle properly*/
    }

    public static PublishResult sendviaSNS(String phoneNumber, String message, Configuration configuration) {
        String accessKey = configuration.getString("aws.accessKeyId");
        String secretKey = configuration.getString("aws.secretKey");
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        final AmazonSNS client = new AmazonSNSClient(awsCredentials);
        // set region to prevent AWS from using default US region 
        // client.setRegion(Region.getRegion(Regions.REGION_YOU_WANT_TO_USE));
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                .withStringValue("BODH") //The sender ID shown on the device.
                .withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                .withStringValue("0.1") //Sets the max price to 0.1 USD.
                .withDataType("Number"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue("Transactional") //Sets the type to promotional.
                .withDataType("String"));

        //<set SMS attributes
        PublishResult result = client.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
        return result;
    }
}
