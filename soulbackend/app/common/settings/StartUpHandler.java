package common.settings;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import models.Otp;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;
import play.Configuration;
import play.api.libs.ws.WSClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Singleton
public class StartUpHandler {
    private static MorphiaObject morphiaObject;
    private static MongoClient mongoClient;
    private static HashSet<String> contentSet;

    public static Datastore getDatastore() {
        return getMorphiaObject().getDatastore();
    }

    @Inject
    public StartUpHandler(WSClient wsClient, Configuration configuration) {
        initMongo(configuration);
        String[] content = configuration.getString("content.Types").split(",");
        contentSet = new HashSet<String>(Arrays.asList(content));
    }

    public static HashSet<String> getContentSet() {
        return contentSet;
    }

    public static MorphiaObject getMorphiaObject() {
        if (morphiaObject == null) {
            throw new IllegalStateException("Morphia not initialized.Call initMongo()");
        } else {
            return morphiaObject;
        }
    }

    protected void initMongo(Configuration config) {
        Morphia morphia = new Morphia();
        MongoClient mongo = this.ensureMongoCLient(config);
        ValidationExtension validationExtension = new ValidationExtension(morphia); // Responsible for enabling validation on morphia
        Datastore ds = morphia.createDatastore(mongo, config.getString("mongo.db"));
        morphia.map(Otp.class);
        ds.ensureIndexes();
        morphiaObject = new MorphiaObject(mongo, morphia, ds);
    }

    protected MongoClient ensureMongoCLient(Configuration configuration) {
        if (mongoClient == null) {
            String serversStr = configuration.getString("mongo.servers");
            String[] serversArr = serversStr.split(",");
            ArrayList<ServerAddress> serverObjs = new ArrayList<>();

            List<MongoCredential> credentialsList = new ArrayList<>();
            for (int i = 0; i < serversArr.length; ++i) {
                serverObjs.add(new ServerAddress(configuration.getString("mongo." + serversArr[i] + ".host"), configuration.getInt("mongo." + serversArr[i] + ".port").intValue()));
                credentialsList.add(MongoCredential.createScramSha1Credential(configuration.getString("mongo." + serversArr[i] + ".username"), configuration.getString("mongo.db"), configuration.getString("mongo." + serversArr[i] + ".password").toCharArray()));

            }
            MongoClientOptions mongoClientOptions = new MongoClientOptions.Builder().socketKeepAlive(true).build();
            mongoClient = new MongoClient(serverObjs, credentialsList, mongoClientOptions);
        }

        return mongoClient;
    }

}
