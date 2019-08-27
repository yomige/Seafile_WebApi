package com.aitlp.test;

import com.aitlp.impl.SeafileApi;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aitlp.data.DirectoryEntry;
import com.aitlp.data.Library;
import com.aitlp.data.UploadFileRes;
import okhttp3.OkHttpClient;

import java.io.File;
import java.util.List;


public class SeafileApiTest {
    public static final String SERVICE_URL = "http://10.47.0.172";
    public static final String FILE_SERVER_ROOT = "http://10.47.0.172/seafhttp";

    public static void main(String[] args) {

//       new SeafileApiTest().testPing();

//       new SeafileApiTest().testObtainAuthToken();

//       new SeafileApiTest().testCheckAccountInfo();

//        new SeafileApiTest().testGetServerInformation();

//        new SeafileApiTest().testListLibraries();

//        new SeafileApiTest().testGetUploadLink();

//        new SeafileApiTest().testListAllDirEntries();

//        new SeafileApiTest().testUploadFile();

//        new SeafileApiTest().testCreateFile();

//        new SeafileApiTest().testCreateNewLibrary();

//        new SeafileApiTest().testDeleteLibrary();

//        new SeafileApiTest().testGetFileDownloadLink();


//        List<StarredFile> starredFiles = api.listStarredFiles(client,token);
//        System.out.println(starredFiles.get(0));
//

//        String deleteLibName="seanote";
//        String delete_repo_id;
//        List<Library> libraries=api.listLibraries(client,token);
//        for (Library library:libraries){
//            if(library.getName().equals(deleteLibName)){
//                delete_repo_id=library.getId();
//                System.out.println(delete_repo_id);
//                api.deleteLibrary(client,token,delete_repo_id);
//            }
//        }


//        String repo_id = api.listLibraries(client, token).get(0).getId();
//        System.out.println("the repo_id = "+repo_id);
//        Library libraryInfo = api.getLibraryInfo(client, token, repo_id);
//        System.out.println("the library name of the repo_id = " + libraryInfo.getName());

//        List<LibraryHistory> historyLists=api.getLibraryHistory(client,token,repo_id);
//        System.out.println(historyLists.get(0).getName());

//        FileDetail fileDetail = api.getFileDetail(client, token, repo_id, "/头像.png");
//        String fileName = fileDetail.getName();
//        String fileId = fileDetail.getId();
//
//        List<FileCommit> fileHistories=api.getFileHistory(client,token,repo_id,"/头像.png");
//        System.out.println(fileHistories.get(0).getId());


//        api.renameFile(client,token,repo_id,"/test.seanote","test2.seanote");
//        api.moveFile(client,token,repo_id,"/test2.seanote",repo_id,"/test");
//        api.deleteFile(client,token,repo_id,"/test.seanote");


//        String updateLink=api.getUpdateLink(client,token,repo_id,"");
//        System.out.println(updateLink);
//
//        api.updateFile(client,token,updateLink,file1,"/test/upload/testfile2.txt");

//        List<DirectoryEntry> directoryEntries=api.listDirEntriesByP(client,token,repo_id,"/test");
//        System.out.println(directoryEntries);

//        List<DirectoryEntry> directoryEntries3=api.listAllDirEntries(client,token,repo_id);
//        System.out.println(directoryEntries3);

//        api.createNewDir(client,token,repo_id,"/test2");
//        api.renameDir(client,token,repo_id,"/test2","test3");
//        api.deleteDir(client,token,repo_id,"/test3");

//        String downDirToken=api.getDirDownloadToken(client,token,repo_id,"/","test");
//        System.out.println("downDirToken = " + downDirToken);

//        boolean dirDownloadFlag1 = api.queryZipProgress(client,token,"66b333cb-7800-4829-bb86-e2d3d715b84d");
//        System.out.println("dirDownloadFlag1 = " + dirDownloadFlag1);


    }


    public void testPing() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);

        String ping = api.ping(client);
        System.out.println("ping = " + ping);
    }


    public void testObtainAuthToken() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);

        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        System.out.println("token = " + token);
    }

    public void testCheckAccountInfo() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);

        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        JSONObject accountInfo = api.checkAccountInfo(client, token);
        System.out.println("account info = " + JSON.toJSONString(accountInfo));
    }

    public void testGetServerInformation() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        JSONObject serverInformation = api.getServerInformation(client);
        System.out.println("server info = " + JSON.toJSONString(serverInformation));
    }

    public void testListLibraries() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);

        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");

        List<Library> list = api.listLibraries(client, token);
        System.out.println(JSONObject.toJSONString(list));
    }


    public void testGetUploadLink() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");

        String uploadLink = api.getUploadLink(client, token, "0de4d65b-732a-4d13-8304-d4f2bc26437e", "");
        System.out.println(uploadLink);
    }

    public void testListAllDirEntries() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");

        List<DirectoryEntry> dirs = api.listAllDirEntries(client, token, "0de4d65b-732a-4d13-8304-d4f2bc26437e");
        System.out.println(dirs.get(0).getParent_dir());
    }


    public void testUploadFile() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        File file1 = new File("E:/项目相关/徐州纪委/elasticsearch/index/删除所有索引.txt");
        List<UploadFileRes> uploadFileResList = api.uploadFile(client, token, "http://10.47.0.172/seafhttp/upload-api/434fce3f-e306-45f8-b47c-d6393cc819f8", "/", "", file1);
        System.out.println(uploadFileResList.size());
    }


    public void testCreateFile() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);

        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.createFile(client, token, "0de4d65b-732a-4d13-8304-d4f2bc26437e", "/aaa/aa1.txt");
    }

    public void testCreateNewLibrary() {
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");

        JSONObject jsonObject = api.createNewLibrary(client, token, "Personal Lib", "我的个人仓库", "Passw0rd");
        System.out.println(JSON.toJSONString(jsonObject));
    }

    public void testDeleteLibrary(){
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        api.deleteLibrary(client,token,"0bcd60f9-3a9a-4498-b1b8-8a365b0c1428");
    }

    public void testGetFileDownloadLink(){
        OkHttpClient client = new OkHttpClient();
        SeafileApi api = new SeafileApi(SERVICE_URL, FILE_SERVER_ROOT);
        String token = api.obtainAuthToken(client, "me@inspur.com", "Passw0rd");
        String downloadLink = api.getFileDownloadLink(client,token,"0de4d65b-732a-4d13-8304-d4f2bc26437e","/删除所有索引.txt",true);
        System.out.println(downloadLink);
    }


}
