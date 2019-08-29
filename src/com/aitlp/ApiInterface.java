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
     * @param p       测试中发现这个链接似乎只会指向资料库的根目录。即使传入了p，所以p似乎是一个无效的参数？
     * @return
     */
    String getUploadLink(OkHttpClient client, String token, String repo_id, String p);

    /**
     * 上传文件（可多文件上传）
     *
     * @param client
     * @param token
     * @param uploadLink
     * @param parent_dir    must endswith "/"
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
     * 删除资料库
     *
     * @param client
     * @param token
     * @param repo_id
     * @return
     */
    boolean deleteLibrary(OkHttpClient client, String token, String repo_id);

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

    /**
     * 创建新账号
     *
     * @param client
     * @param token
     * @param email
     * @param password
     * @return
     */
    boolean createNewAccount(OkHttpClient client, String token, String email, String password);

    /**
     * 删除账号
     *
     * @param client
     * @param token
     * @param email
     * @return
     */
    boolean deleteAccount(OkHttpClient client, String token, String email);

    /**
     * 星标文件列表
     *
     * @param client
     * @param token
     * @return
     */
    List<StarredFile> listStarredFiles(OkHttpClient client, String token);

    /**
     * 获取资料库信息
     *
     * @param client
     * @param token
     * @param repo_id 资料库ID
     * @return
     */
    Library getLibraryInfo(OkHttpClient client, String token, String repo_id);

    /**
     * 查看资料库历史
     *
     * @param client
     * @param token
     * @param repo_id 资料库ID
     * @return
     */
    List<LibraryHistory> getLibraryHistory(OkHttpClient client, String token, String repo_id);

    /**
     * 文件明细
     *
     * @param client
     * @param token
     * @param repo_id 仓库地址
     * @param p       文件路径，入"/Documents/readme.txt"
     * @return
     */
    FileDetail getFileDetail(OkHttpClient client, String token, String repo_id, String p);

    /**
     * 文件历史
     *
     * @param client
     * @param token
     * @param repo_id
     * @param p       文件路径，入"/Documents/readme.txt"
     * @return
     */
    List<FileCommit> getFileHistory(OkHttpClient client, String token, String repo_id, String p);

    /**
     * 文件重命名（接口有问题？命名成功，但301跳转的地址不对 已提 issue:https://github.com/haiwen/seafile/issues/2238）
     *
     * @param client
     * @param token
     * @param repo_id
     * @param p       文件路径，入"/Documents/readme.txt"
     * @param newName 新的文件名
     * @return
     */
    boolean renameFile(OkHttpClient client, String token, String repo_id, String p, String newName);

    /**
     * 移动文件
     *
     * @param client
     * @param token
     * @param repo_id
     * @param p
     * @param dst_repo 目标资料库
     * @param dst_dir  目标路径
     * @return
     */
    boolean moveFile(OkHttpClient client, String token, String repo_id, String p, String dst_repo, String dst_dir);

    /**
     * 回滚文件
     *
     * @param client
     * @param token
     * @param repo_id
     * @param p
     * @param commit_id 历史版本号
     * @return
     */
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
