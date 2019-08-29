package com.aitlp.impl;

import com.aitlp.ApiInterface;
import com.aitlp.data.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 接口实现
 */
public class SeafileApi implements ApiInterface {
    // SERVICE_URL and FILE_SERVER_ROOT NOT endswith "/";
    private String SERVICE_URL;
    private String FILE_SERVER_ROOT;

    public SeafileApi(String SERVICE_URL, String FILE_SERVER_ROOT) {
        this.SERVICE_URL = SERVICE_URL;
        this.FILE_SERVER_ROOT = FILE_SERVER_ROOT;
    }

    @Override
    public String ping(OkHttpClient client) {
        Request request = new Request.Builder()
                .get()
                .url(SERVICE_URL + "/api2/ping/")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String obtainAuthToken(OkHttpClient client, String username, String password) {
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(SERVICE_URL + "/api2/auth-token/")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            JSONObject jObj = parseJson(response.body().string());
            return jObj.getString("token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject checkAccountInfo(OkHttpClient client, String token) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .get()
                .url(SERVICE_URL + "/api2/account/info/")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return parseJson(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject getServerInformation(OkHttpClient client) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .get()
                .url(SERVICE_URL + "/api2/server-info/")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return parseJson(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Library> listLibraries(OkHttpClient client, String token) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .get()
                .url(SERVICE_URL + "/api2/repos")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return JSON.parseArray(response.body().string(), Library.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DirectoryEntry> listAllDirEntries(OkHttpClient client, String token, String repo_id) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/dir/?t=d&recursive=1")
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return JSONObject.parseArray(response.body().string(), DirectoryEntry.class);
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * notice
     * when you use this method to get the DirectoryEntry of the dir ,the parent_dir maybe is null
     * if you want to get all parameter in the DirectoryEntry of the dir ,you need to use the next method
     * List<DirectoryEntry> listAllDirEntries()
     *
     * @param client
     * @param token
     * @param repo_id
     * @param p
     * @return
     */
    @Override
    public List<DirectoryEntry> listDirEntriesByP(OkHttpClient client, String token, String repo_id, String p) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/dir/?p=" + p)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return JSONObject.parseArray(response.body().string(), DirectoryEntry.class);
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUploadLink(OkHttpClient client, String token, String repo_id, String p) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/upload-link/?p=" + p)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string().replaceAll("\"", "");
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UploadFileRes> uploadFile(OkHttpClient client, String token, String uploadLink, String parent_dir, String relative_path, File... files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for (File file : files) {
            builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
        }

        builder.addFormDataPart("parent_dir", parent_dir);
        builder.addFormDataPart("relative_path", relative_path);
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .post(body)
                .url(uploadLink + "?ret-json=1")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return JSONObject.parseArray(response.body().string(), UploadFileRes.class);
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean createFile(OkHttpClient client, String token, String repo_id, String p) {
        RequestBody body = new FormBody.Builder()
                .add("operation", "create")
                .build();
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/file/?p=" + p)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public JSONObject createNewLibrary(OkHttpClient client, String token, String libName, String desc, String password) {
        FormBody.Builder builder = new FormBody.Builder()
                .add("name", libName);
        if (desc != null) {
            builder.add("desc", desc);
        }
        if (password != null) {
            builder.add("password", password);
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .post(requestBody)
                .url(SERVICE_URL + "/api2/repos/")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return parseJson(response.body().string());
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteLibrary(OkHttpClient client, String token, String repo_id) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .delete()
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getFileDownloadLink(OkHttpClient client, String token, String repo_id, String p, boolean reuse) {
        String reuse_Temp = reuse ? "&reuse=1" : "";
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .get()
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/file/?p=" + p + reuse_Temp)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean createNewAccount(OkHttpClient client, String token, String email, String password) {
        RequestBody body = new FormBody.Builder()
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api2/accounts/" + email + "/")
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAccount(OkHttpClient client, String token, String email) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api2/accounts/" + email + "/")
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println(response.code());
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<StarredFile> listStarredFiles(OkHttpClient client, String token) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .get()
                .url(SERVICE_URL + "/api2/starredfiles/")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return JSON.parseArray(response.body().string(), StarredFile.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Library getLibraryInfo(OkHttpClient client, String token, String repo_id) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .get()
                .url(SERVICE_URL + "/api2/repos/" + repo_id)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return JSON.parseObject(response.body().string(), Library.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<LibraryHistory> getLibraryHistory(OkHttpClient client, String token, String repo_id) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .get()
                .url(SERVICE_URL + "/api/v2.1/repos/" + repo_id + "/history/")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return JSONObject.parseArray(parseJson(response.body().string()).getString("data"), LibraryHistory.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FileDetail getFileDetail(OkHttpClient client, String token, String repo_id, String p) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .get()
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/file/detail/?p=" + p)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return JSONObject.parseObject(response.body().string(), FileDetail.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FileCommit> getFileHistory(OkHttpClient client, String token, String repo_id, String p) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .get()
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/file/history/?p=" + p)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return JSONObject.parseArray(parseJson(response.body().string()).getString("commits"), FileCommit.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean renameFile(OkHttpClient client, String token, String repo_id, String p, String newName) {
        RequestBody body = new FormBody.Builder()
                .add("operation", "rename")
                .add("newname", newName)
                .build();
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/file/?p=" + p)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean moveFile(OkHttpClient client, String token, String repo_id, String p, String dst_repo, String dst_dir) {
        RequestBody body = new FormBody.Builder()
                .add("operation", "move")
                .add("dst_repo", dst_repo)
                .add("dst_dir", dst_dir)
                .build();
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/file/?p=" + p)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean revertFile(OkHttpClient client, String token, String repo_id, String p, String commit_id) {
        RequestBody body = new FormBody.Builder()
                .add("commit_id", commit_id)
                .add("p", p)
                .build();
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/file/revert/")
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteFile(OkHttpClient client, String token, String repo_id, String p) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/file/?p=" + p)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println(response.code());
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getUpdateLink(OkHttpClient client, String token, String repo_id, String p) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/update-link/?p=" + p)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string().replaceAll("\"", "");
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateFile(OkHttpClient client, String token, String updateLink, File file, String target_file) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));

        builder.addFormDataPart("filename", file.getName());
        builder.addFormDataPart("target_file", target_file);
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .post(body)
                .url(updateLink)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createNewDir(OkHttpClient client, String token, String repo_id, String p) {
        RequestBody body = new FormBody.Builder()
                .add("operation", "mkdir")
                .build();
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/dir/?p=" + p)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean renameDir(OkHttpClient client, String token, String repo_id, String p, String newName) {
        RequestBody body = new FormBody.Builder()
                .add("operation", "rename")
                .add("newname", newName)
                .build();
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/dir/?p=" + p)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteDir(OkHttpClient client, String token, String repo_id, String p) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api2/repos/" + repo_id + "/dir/?p=" + p)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getDirDownloadToken(OkHttpClient client, String token, String repo_id, String parent_dir, String dirents) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api/v2.1/repos/" + repo_id + "/zip-task/?parent_dir=" + parent_dir + "&dirents=" + dirents)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return JSONObject.parseObject(response.body().string()).getString("zip_token");
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getDirDownloadLink(OkHttpClient client, String token, String dirDownloadToken) {
        if (queryZipProgress(client, token, dirDownloadToken)) {
            return FILE_SERVER_ROOT + "/zip/" + dirDownloadToken;
        }
        return null;
    }

    @Override
    public boolean queryZipProgress(OkHttpClient client, String token, String dirDownloadToken) {
        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .header("indent", "4")
                .url(SERVICE_URL + "/api/v2.1/query-zip-progress/?token=" + dirDownloadToken)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String temp = response.body().string();
                if (JSONObject.parseObject(temp).getString("zipped").equals(
                        JSONObject.parseObject(temp).getString("total")
                )) {
                    return true;
                } else {
                    return false;
                }
            } else {
                System.out.println(response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * parse json String to JsonObject
     *
     * @param jsonStr
     * @return
     */
    private JSONObject parseJson(String jsonStr) {
        return JSON.parseObject(jsonStr, Feature.AutoCloseSource);
    }
}
