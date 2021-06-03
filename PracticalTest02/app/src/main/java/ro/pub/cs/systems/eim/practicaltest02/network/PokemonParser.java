package ro.pub.cs.systems.eim.practicaltest02.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02.model.PokemonInformation;

public class PokemonParser implements DataParser {

    private String uri;
    private TextView informationTextView;
    private ImageView profileImageView;
    private Bitmap bitmapImage;

    public PokemonParser(String uri, TextView informationTextView, ImageView profileImageView) {
        this.uri = uri;
        this.informationTextView = informationTextView;
        this.profileImageView = profileImageView;
    }

    @Override
    public void sendRequest(Socket socket) throws IOException {
        BufferedReader bufferedReader = Utilities.getReader(socket);
        PrintWriter printWriter = Utilities.getWriter(socket);

        printWriter.println(uri);
        printWriter.flush();

        String pageSourceCode;
        while ((pageSourceCode = bufferedReader.readLine()) != null) {
            PokemonInformation pokemonInformation = parseJson(pageSourceCode);

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGetCartoon = new HttpGet(pokemonInformation.getImageURI());
            HttpResponse httpResponse = httpClient.execute(httpGetCartoon);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                bitmapImage = BitmapFactory.decodeStream(httpEntity.getContent());
                profileImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        profileImageView.setImageBitmap(bitmapImage);
                    }
                });
            }
            final String information = pokemonInformation.toString();
            informationTextView.post(new Runnable() {
                @Override
                public void run() {
                    informationTextView.setText(information);
                }
            });
        }
    }

    private PokemonInformation parseJson(String pageSourceCode) {
        JSONObject content = null;
        try {
            content = new JSONObject(pageSourceCode);

            JSONArray types = content.getJSONArray("types");
            JSONArray abilities = content.getJSONArray("abilities");
            String uri = content.getJSONObject("sprites").getString("front_default");

            String[] typeNames = new String[types.length()];
            for (int i = 0; i < types.length(); i++) {
                JSONObject type = ((JSONObject) types.get(i)).getJSONObject("type");
                String typeName = type.getString("name");
                typeNames[i] = typeName;
            }

            String[] abilityNames = new String[abilities.length()];
            for (int i = 0; i < abilities.length(); i++) {
                JSONObject ability = ((JSONObject) abilities.get(i)).getJSONObject("ability");
                String abilityName = ability.getString("name");
                abilityNames[i] = abilityName;
            }
            return new PokemonInformation(
                    typeNames, abilityNames, uri
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new PokemonInformation(new String[]{""}, new String[]{""}, "");
    }
}
