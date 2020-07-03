package com.example.myapplication.sync.restTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.myapplication.DTO.UserDto;
import com.example.myapplication.database.DBContentProvider;
import com.example.myapplication.database.SqlHelper;
import com.example.myapplication.util.SavePictureUtil;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class UserTask extends AsyncTask<String, Void, ResponseEntity<UserDto>> {

    private Context context;

    public UserTask(Context context) {this.context = context;}

    @Override
    protected ResponseEntity<UserDto> doInBackground(String... strings) {
        final String url = strings[0];
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity entity = new HttpEntity(headers);

            ResponseEntity<UserDto> response = restTemplate.getForEntity(url, UserDto.class, entity);

            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(ResponseEntity<UserDto> responseEntity) {

        if(responseEntity != null) {
            UserDto userDto = responseEntity.getBody();

            SqlHelper dbHelper = new SqlHelper(context);
            dbHelper.dropUserTable();

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues entryUser = new ContentValues();

            entryUser.put(SqlHelper.COLUMN_USER_MYSQLID, userDto.getId());
            entryUser.put(SqlHelper.COLUMN_USER_NAME, userDto.getName());
            entryUser.put(SqlHelper.COLUMN_USER_SURNAME, userDto.getSurname());
            entryUser.put(SqlHelper.COLUMN_USER_PHONE_NUMBER, userDto.getPhoneNumber());
            entryUser.put(SqlHelper.COLUMN_USER_EMAIL, userDto.getEmail());
            entryUser.put(SqlHelper.COLUMN_USER_PICTURE, userDto.getPictureName());
            SavePictureUtil.writeToFile(userDto.getPicture(), userDto.getPictureName(), context, context.getFilesDir());
            Uri userUri = context.getContentResolver().insert(DBContentProvider.CONTENT_URI_USER, entryUser);
            db.close();

        } else {
            System.out.println("promeniti ip adresu");
        }

    }
}
