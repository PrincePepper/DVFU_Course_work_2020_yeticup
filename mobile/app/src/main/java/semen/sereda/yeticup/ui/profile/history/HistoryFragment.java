package semen.sereda.yeticup.ui.profile.history;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

import semen.sereda.yeticup.R;
import semen.sereda.yeticup.ui.profile.history.element.CompClass;
import semen.sereda.yeticup.ui.profile.history.element.HistoryClass;
import semen.sereda.yeticup.ui.profile.history.element.SimpleFrag;

import static semen.sereda.yeticup.SplashScreenActivity.ThisUser;

public class HistoryFragment extends Fragment {
    public static ArrayList<HistoryClass> CompetitionUserList = new ArrayList<>();
    public static ArrayList<CompClass> CompetitionList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history_profile, container, false);
        new JsonGetCompetition().execute();
        new JsonGetTask().execute();

        if (CompetitionUserList.size() != 0) {
            for (int i = 0; i < CompetitionUserList.size(); i++) {
                for (int j = 0; j < CompetitionList.size(); j++) {
                    if (CompetitionList.get(j).year == CompetitionUserList.get(i).date)
                        CompetitionUserList.get(i).name = CompetitionList.get(j).name;
                }
            }

            for (HistoryClass i : CompetitionUserList) {
                if (savedInstanceState == null) {
                    getFragmentManager()
                            .beginTransaction()
                            .add(R.id.selection, SimpleFrag.newInstance(i))
                            .commit();
                }
            }

        } else {
            TextView textView = root.findViewById(R.id.textView15);
            textView.setVisibility(View.VISIBLE);
        }
        return root;
    }

    public static class JsonParser {
        public void getUser(String response) throws JSONException {
            if (!response.equals("[]\n")) {
                JSONArray userJson = new JSONArray(response);
                for (int i = 0; i < userJson.length(); i++) {
                    JSONObject row = userJson.getJSONObject(i);
                    String name = row.getString("user_id");
                    String role = row.getString("role");
                    String year = row.getString("year");
                    CompetitionUserList.add(new HistoryClass(name, year, role));
                }
            }

        }
    }

    private static class JsonGetCompetition extends AsyncTask<String, String, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            JsonParserCompetition jsonParser = new JsonParserCompetition();

            try {
                URL url = new URL("https://yetiapi.herokuapp.com/api/competitions/");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                String encoded = Base64.getEncoder().encodeToString(("yeti" + ":" + "yetiyeti").getBytes(StandardCharsets.UTF_8));

                connection.setRequestProperty("Authorization", "Basic " + encoded);
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.e("AAAAAAAAAAA", line);
                    buffer.append(line).append("\n");
                }
                jsonParser.getComp(buffer.toString());
                return buffer.toString();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "{}";
        }

        @Override
        protected void onPostExecute(String u) {
        }
    }

    public static class JsonParserCompetition {
        public void getComp(String response) throws JSONException {
            JSONArray userJson = new JSONArray(response);
            for (int i = 0; i < userJson.length(); i++) {
                JSONObject row = userJson.getJSONObject(i);
                String year = row.getString("year");
                String name = row.getString("name");
                String address = row.getString("address");
                String date = row.getString("date");
                String total_participants = row.getString("total_participants");
                CompetitionList.add(new CompClass(year, name, address, date, total_participants));
            }
        }
    }

    private class JsonGetTask extends AsyncTask<String, String, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            JsonParser jsonParser = new JsonParser();

            try {
                URL url = new URL("https://yetiapi.herokuapp.com/api/users/" + ThisUser.id + "/get_participants/");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                String encoded = Base64.getEncoder().encodeToString(("yeti" + ":" + "yetiyeti").getBytes(StandardCharsets.UTF_8));

                connection.setRequestProperty("Authorization", "Basic " + encoded);
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.e("AAAAAAAAAAA", line);
                    buffer.append(line).append("\n");
                }
                jsonParser.getUser(buffer.toString());
                return buffer.toString();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "{}";
        }

        @Override
        protected void onPostExecute(String u) {
        }
    }
}