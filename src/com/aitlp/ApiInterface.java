package com.aitlp;

import com.aitlp.data.*;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;

import java.io.File;
import java.util.List;

/**
 * Seafile Api 接口
 */
public interface ApiInterface {

    /**
     * 测试服务状态
     *
     * @param client
     * @return
     */
    String ping(OkHttpClient client);

    /**
     * 获取token
     *
     * @param client
     * @param username 账号
     * @param password 密码
     * @return
     */
    String obtainAuthToken(OkHttpClient client, String username, String password);

    /**
     * 获取账号信息
     *
     * @param client
     * @param token
     * @return
     */
    JSONObject checkAccountInfo(OkHttpClient client, String token);


    /**
     * 获取seafile服务端版本等信息
     *
     * @param client
     * @return
     */
    JSONObject getServerInformation(OkHttpClient client);

    /**
     * 列出所有资料库
     *
     * @param client
     * @param token
     * @return
     */
    List<Library> listLibraries(OkHttpClient client, String token);

    /**
     * 列出某资料库下的所有文件夹
     *
     * @param client
     * @param token
     * @param repo_id
     * @return
     */
    List<DirectoryEntry> listAllDirEntries(OkHttpClient client, String token, String repo_id);

    /**
     * 获取上传接口链接
     *
     * @param client
     * @param token
     * @param repo_id
     * @param p
     * @return
     */
    String getUploadLink(OkHttpClient client, String token, String repo_id, String p);

    /**
     * 上传文件（可多文件上传）
     *
     * @param client
     * @param token
     * @param uploadLink
     * @param parent_dir must endswith "/"
     * @param relative_path must NOT startswith "/"
     * @param files
     * @return
     */
    List<UploadFileRes> uploadFile(OkHttpClient client, String token, String uploadLink, String parent_dir, String relative_path, File... files);

    /**
     * 新建文件
     *
     * @param client
     * @param token
     * @param repo_id
     * @param p
     * @return
     */
    boolean createFile(OkHttpClient client, String token, String repo_id, String p);

    /**
     * 创建个人资料库
     *
     * @param client
     * @param token
     * @param libName
     * @param desc     非必须
     * @param password 非必须
     * @return
     */
    JSONObject createNewLibrary(OkHttpClient client, String token, String libName, String desc, String password);


    /**
     * 获取文件下载链接
     *
     * @param client
     * @param token
     * @param repo_id
     * @param p       文件路径
     * @param reuse   下载链接是否复用
     * @return
     */
    String getFileDownloadLink(OkHttpClient client, String token, String repo_id, String p, boolean reuse);


    //Starred File API
    List<StarredFile> listStarredFiles(OkHttpClient client, String token);


    Library getLibraryInfo(OkHttpClient client, String token, String repo_id);

    List<LibraryHistory> getLibraryHistory(OkHttpClient client, String token, String repo_id);

    boolean deleteLibrary(OkHttpClient client, String token, String repo_id);

    FileDetail getFileDetail(OkHttpClient client, String token, String repo_id, String p);

    List<FileCommit> getFileHistory(OkHttpClient client, String token, String repo_id, String p);


    boolean renameFile(OkHttpClient client, String token, String repo_id, String p, String newName);

    boolean moveFile(OkHttpClient client, String token, String repo_id, String p, String dst_repo, String dst_dir);

    boolean revertFile(OkHttpClient client, String token, String repo_id, String p, String commit_id);

    boolean deleteFile(OkHttpClient client, String token, String repo_id, String p);

    String getUpdateLink(OkHttpClient client, String token, String repo_id, String p);

    boolean updateFile(OkHttpClient client, String token, String updataLink, File file, String target_file);

    //Directory API
    List<DirectoryEntry> listDirEntriesByP(OkHttpClient client, String token, String repo_id, String p);

//    List<DirectoryEntry> listDirectoryEntriesByID(OkHttpClient client,String token,String repo_id,String id);


    boolean createNewDir(OkHttpClient client, String token, String repo_id, String p);

    boolean renameDir(OkHttpClient client, String token, String repo_id, String p, String newName);

    boolean deleteDir(OkHttpClient client, String token, String repo_id, String p);

    String getDirDownloadToken(OkHttpClient client, String token, String repo_id, String parent_dir, String dirents);

    boolean queryZipProgress(OkHttpClient client, String token, String dirDownloadToken);

    String getDirDownloadLink(OkHttpClient client, String token, String dirDownloadToken);
}
