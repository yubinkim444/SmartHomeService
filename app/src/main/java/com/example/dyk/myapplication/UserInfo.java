package com.example.dyk.myapplication;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by msi on 2017-05-31.
 */

public class UserInfo {
    public Context context;
    static  UserInfo userInfo = new UserInfo();
    public String tempApikey = "5ZzRBf03rhE/V6evtcZoZEwjImRB3U0z7ecCVGEOTs1pr0+cYU05VReQJ3tM3r7yQW4F0+kuqbEMdxMVQlUA9g==";
    public String humdApiKey = "2IAamV43ndgNz2oovd5tHEFfTR/Er8svCIAzkAeg/9mtV2lTjNMEZcMSePHj4GDEZ/J+CaD2f/aY7ztyQLwybQ==";
    public String luxApikey ="2Sd85bbtkWoKCdX9S3VxN9tMgbnVYMGI+UaL16sZzct0s/mof2yFjeUacbP3Ysq9rhp/9sPRaMqQFjNqSyO43w==";
    public String tempUrl ="https://ussouthcentral.services.azureml.net/workspaces/b3428fb2112c4f288743ddb313bad8d7/services/653c68353b3f4fa59aef7e132f5747be/execute?api-version=2.0&format=swagger";
    public String humdUrl ="https://ussouthcentral.services.azureml.net/workspaces/b3428fb2112c4f288743ddb313bad8d7/services/7e4a34ebc6fe4406b9395dcf0d81fd7c/execute?api-version=2.0&format=swagger";
    public String luxUrl ="https://ussouthcentral.services.azureml.net/workspaces/b3428fb2112c4f288743ddb313bad8d7/services/ff3ffc5482c3459bae05027422443a48/execute?api-version=2.0&format=swagger";

    public ArrayList<HashMap<String,String>> myInfo;
    public ArrayList<HashMap<String,String>> roomList;
    public ArrayList<ArrayList<HashMap<String,String>>> roomData = new ArrayList<ArrayList<HashMap<String, String>>>();
    public  int clickedTab;
    public  ArrayList<HashMap<String,String>>[] resTab = new ArrayList[10];
    public boolean autoServiceOff[] = {false,false,false,false,false,false,false,false,false,false,false};

}
