package cmput301f17t09.goalsandhabits.ElasticSearch;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.Profiles.Profile;
import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Andrew on 11/23/2017.
 */

public class ElasticSearchController {
    public static final String appESURL = "http://cmput301.softwareprocess.es:8080/";
    public static final String appESIndex = "cmput301f17t09_goalsandhabits";

    private static JestDroidClient client;

    public static class AddProfileTask extends AsyncTask<Profile, Void, Void> {

        @Override
        protected Void doInBackground(Profile... profiles){
            verifySettings();

            for (Profile profile: profiles){
                Index index = new Index.Builder(profile).index(appESIndex).type("profile").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()){
                        profile.setUserId(result.getId());
                    }else{
                        Log.i("Error","Elasticsearch was unable to add profile");
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    //Log.i("Error", "The app failed to build and send profile " + e.getCause());
                }
            }
            return null;
        }
    }

    public static class GetProfileTask extends AsyncTask<String, Void, Profile>{
        @Override
        protected Profile doInBackground(String... parameters){
            verifySettings();

            Profile profile = null;

            Get get = new Get.Builder(appESIndex, parameters[0]).type("profile").build();

            try {
                JestResult result = client.execute(get);
                if (result.isSucceeded()) {
                    profile = (result.getSourceAsObject(Profile.class));
                }
            }
            catch (Exception e){
                Log.i("Error", "ElasticSearch failed to find profile");
            }
            return profile;
        }
    }

    public static class GetProfilesTask extends AsyncTask<String, Void, ArrayList<Profile>>{
        @Override
        protected ArrayList<Profile> doInBackground(String... search_paramaters){
            verifySettings();

            ArrayList<Profile> profiles = new ArrayList<Profile>();

            String query = "{\n"
                    + "   \"query\" : {\n"
                    + "        \"term\" : { \"username\" : \""+search_paramaters[0]+"\"}\n"
                    + "    }\n"
                    + "}";

            Search search = new Search.Builder(query).addIndex(appESIndex).addType("profile").build();

            try{
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){
                    List<Profile> foundProfiles = result.getSourceAsObjectList(Profile.class);
                    profiles.addAll(foundProfiles);
                }else{
                    Log.i("Error","ElasticSearch search query failed");
                }
            }catch (Exception e){
                Log.i("Error","ElasticSearch filed to find profiles");
            }
            return profiles;
        }
    }

    public static void verifySettings(){
        if (client == null){
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(appESURL);
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
