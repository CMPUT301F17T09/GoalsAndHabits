package cmput301f17t09.goalsandhabits.ElasticSearch;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.Profiles.Profile;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.MultiGet;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Andrew on 11/23/2017.
 */

public class ElasticSearchController {
    public static final String appESURL = "http://cmput301.softwareprocess.es:8080/";
    public static final String appESIndex = "cmput301f17t09_goalsandhabits";

    private static JestDroidClient client;

    /**
     * Add a profile to the elasticsearch server.
     * Note: Only add one profile per task. (Shouldn't ever need to add more than one at a time anyways)
     */
    public static class AddProfileTask extends AsyncTask<Profile, Void, Profile> {

        @Override
        protected Profile doInBackground(Profile... profiles){
            verifySettings();

            Profile retProfile = null;
            for (Profile profile: profiles){
                Index index = new Index.Builder(profile).index(appESIndex).type("profile").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()){
                        profile.setUserId(result.getId());
                        retProfile = profile;
                    }else{
                        Log.i("Error","Elasticsearch was unable to add profile");
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    //Log.i("Error", "The app failed to build and send profile " + e.getCause());
                }
            }
            return retProfile;
        }
    }

    public static class UpdateProfileTask extends AsyncTask<Profile, Void, Void> {
        @Override
        protected Void doInBackground(Profile... profiles){
            verifySettings();

            for (Profile profile: profiles){
                if (profile.getUserId()!=null) {
                    Index index = new Index.Builder(profile).index(appESIndex).type("profile").id(profile.getUserId()).build();

                    try {
                        DocumentResult result = client.execute(index);
                        if (result.isSucceeded()) {
                            profile.setUserId(result.getId());
                        } else {
                            Log.i("Error", "Elasticsearch was unable to update profile: " + result.getErrorMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Log.i("Error", "The app failed to build and send profile " + e.getCause());
                    }
                }
            }
            return null;
        }
    }

    public static class AddHabitsTask extends AsyncTask<Habit, Void, Void> {

        @Override
        protected Void doInBackground(Habit... habits){
            verifySettings();
            Index index = null;
            List<Index> indexes = new ArrayList<>();

            for (Habit habit : habits) {
                if (habit.getId() != null) {
                    index = new Index.Builder(habit).id(habit.getId()).build();
                    indexes.add(index);
                }
            }
            if (indexes.isEmpty()){
                return null;
            }
            Bulk bulk = new Bulk.Builder()
                    .defaultIndex(appESIndex)
                    .defaultType("habit")
                    .addAction(indexes)
                    .build();

            try {
                JestResult result = client.execute(bulk);
                if (result.isSucceeded()){
                    Log.i("Info","Successfully added habits.");
                }
                /*client.executeAsync(bulk, new JestResultHandler<JestResult>() {
                    @Override
                    public void completed(JestResult result){
                        if (result.isSucceeded()){
                            Log.i("Info","Successfully added habits.");
                        }
                    }
                    @Override
                    public void failed(Exception e){
                        e.printStackTrace();
                    }
                });*/
            }catch (Exception e){
                Log.i("Error", "The app failed to build and send habits");
            }
            return null;
        }
    }
    /*
    public static class UpdateHabitsTask extends AsyncTask<Habit, Void, Void> {

        @Override
        protected Void doInBackground(Habit... habits){
            verifySettings();

            for (Habit habit : habits){
                if (habit.getId()!=null) {
                    Index index = new Index.Builder(habit).index(appESIndex).type("habit").id(habit.getId()).build();

                    try {
                        DocumentResult result = client.execute(index);
                        if (result.isSucceeded()){
                            Log.i("Info", "Successfully updated habit " + habit.getId());
                        }else{
                            Log.i("Error", "Failed to update habit " + habit.getId());
                        }
                    }catch (Exception e){
                        Log.i("Error", "The app failed to update habit");
                    }
                }
            }
            return null;
        }
    }*/

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

    /**
     * Task for getting habits from elastic search server.
     * Parameters are a list of habit ids to fetch
     */
    public static class GetHabitsTask extends AsyncTask<String, Void, ArrayList<Habit>>{
        @Override
        protected ArrayList<Habit> doInBackground(String... search_parameters){
            verifySettings();

            MultiGet mget = new MultiGet.Builder.ById(appESIndex, "habit").addId(Arrays.asList(search_parameters)).build();

            ArrayList<Habit> habits = new ArrayList<>();
            try {
                JestResult result = client.execute(mget);
                if (result.isSucceeded()){
                    habits = new ArrayList<>(result.getSourceAsObjectList(Habit.class));
                    Log.i("Info","Got " + habits.size() + " habits.");
                }else{
                    Log.i("Error","Getting habits failed due to: " + result.getErrorMessage());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return habits;
        }
    }

    public static class DeleteHabitTask extends AsyncTask<Habit, Void, Void>{
        @Override
        protected Void doInBackground(Habit... habits){
            verifySettings();

            ArrayList<Delete> deletions = new ArrayList<>();

            for (Habit habit : habits) {
                if (habit.getId()!=null) {
                    Delete delete = new Delete.Builder(habit.getId()).index(appESIndex).type("habit").build();
                    deletions.add(delete);
                }
            }
            if (!deletions.isEmpty()){
                Bulk bulk = new Bulk.Builder()
                        .defaultIndex(appESIndex)
                        .defaultType("habit")
                        .addAction(deletions)
                        .build();

                try{
                    JestResult result = client.execute(bulk);
                    if (result.isSucceeded()){
                        Log.i("Info","Successfully deleted habits");
                    }
                }catch (Exception e){
                    Log.i("Error","Failed to delete habits");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    public static class GetProfilesTask extends AsyncTask<String, Void, ArrayList<Profile>>{
        @Override
        protected ArrayList<Profile> doInBackground(String... search_parameters){
            verifySettings();

            ArrayList<Profile> profiles = new ArrayList<Profile>();

            String query = "{\n"
                    + "   \"query\" : {\n"
                    + "        \"term\" : { \"username\" : \""+search_parameters[0]+"\"}\n"
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
