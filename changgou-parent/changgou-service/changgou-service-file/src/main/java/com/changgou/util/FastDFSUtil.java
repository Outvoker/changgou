package com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Xu Rui
 * @date 2021/2/20 23:07
 * 实现FastDFS文件管理
 *  文件上传
 *  文件删除
 *  文件下载
 *  文件信息获取
 *  Storage信息获取
 *  Tracker信息获取
 */
public class FastDFSUtil {

    /**
     * 加载Tracker连接信息
     */
    static{
        try {
            //查找classpath下的文件路径
            String path = new ClassPathResource("fdfs_client.conf").getPath();
            //加载Tracker连接信息
            ClientGlobal.init(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     * @param fastDFSFile   封装文件信息
     * @throws IOException  IOException
     * @throws MyException  MyException
     * @return uploads
     *             uploads[0]:文件上传所存储的Storage的组名字
     *             uploads[1]:文件存储到Storage上的文件路径名字
     */
    public static String[] upload(FastDFSFile fastDFSFile) throws IOException, MyException {
        //获取StorageClient
        StorageClient storageClient = getStorageClient();

        /*
        通过StorageClient访问Storage，实现文件上传，并且获取文件上传后的存储信息
            1.上传文件的字节数组
            2.文件的扩展名
            3.附加参数
        uploads
            uploads[0]:文件上传所存储的Storage的组名字
            uploads[1]:文件存储到Storage上的文件路径名字
         */
        return storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), fastDFSFile.getMetaList());
    }

    /**
     * 获取文件信息
     * @param groupName         文件的组名
     * @param remoteFileName    文件的存储路径名
     * @return                  FileInfo
     * @throws IOException      IOException
     * @throws MyException      MyException
     */
    public static FileInfo getFileInfo(String groupName, String remoteFileName) throws IOException, MyException {
        StorageClient storageClient = getStorageClient();

        //获取文件信息
        return storageClient.get_file_info(groupName, remoteFileName);
    }

    /**
     * 文件下载
     * @param groupName         文件的组名
     * @param remoteFileName    文件的存储路径名
     * @return                  FileInfo
     * @throws IOException      IOException
     * @throws MyException      MyException
     */
    public static InputStream downloadFile(String groupName, String remoteFileName) throws IOException, MyException {
        StorageClient storageClient = getStorageClient();

        //下载文件
        byte[] bytes = storageClient.download_file(groupName, remoteFileName);
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 删除文件
     * @param groupName         文件的组名
     * @param remoteFileName    文件的存储路径名
     * @throws IOException      IOException
     * @throws MyException      MyException
     */
    public static void deleteFile(String groupName, String remoteFileName) throws IOException, MyException {
        StorageClient storageClient = getStorageClient();

        //删除文件
        storageClient.delete_file(groupName, remoteFileName);
    }

    /**
     * 获取Storage信息
     * @return  StorageServer
     * @throws IOException  IOException
     */
    public static StorageServer getStorage() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getStoreStorage(trackerServer);
    }

    /**
     * 获取Storage的IP和端口信息
     * @param groupName         文件的组名
     * @param remoteFileName    文件的存储路径名
     * @return                  ServerInfo[]
     * @throws IOException      IOException
     */
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName) throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    /**
     * 获取Tracker信息
     * @return  url
     * @throws IOException  IOException
     */
    public static String getTrackerInfo() throws IOException {
        TrackerServer trackerServer = getTrackerServer();

        int tracker_http_port = ClientGlobal.getG_tracker_http_port();
        String ip = trackerServer.getInetSocketAddress().getHostString();
        return "http://" + ip + ":" + tracker_http_port;
    }

    /**
     * 获取Tracker Server
     * @return  TrackerServer
     * @throws IOException  IOException
     */
    public static TrackerServer getTrackerServer() throws IOException {
        //创建一个Tracker访问的客户端对象
        TrackerClient trackerClient = new TrackerClient();

        //通过TrackerClient访问TrackerServer服务，获取连接信息
        return trackerClient.getConnection();
    }

    /**
     * 获取Storage Client
     * @return  StorageClient
     * @throws IOException  IOException
     */
    public static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        return new StorageClient(trackerServer, null);
    }

    public static void main(String[] args) throws IOException, MyException {
//        FileInfo fileInfo = getFileInfo("group1", "M00/00/00/CrFJKGAxMxuAVOzAAAAALVfxeaA671.csv");
//        System.out.println(fileInfo);

//        InputStream inputStream = downloadFile("group1", "M00/00/00/CrFJKGAxMxuAVOzAAAAALVfxeaA671.csv");
//        int i = 0;
//        while((inputStream.read()) != -1) i++;
//        System.out.println(i);
//"http://10.177.73.40:8080/group1/M00/00/00/CrFJKGAyIjuAL2wQAACVfPJeN6Y575.jpg"
//        deleteFile("group1", "M00/00/00/CrFJKGAyINaAOIn3AADMNC3voZY99.jpeg");
//        StorageServer storage = getStorage();
//        System.out.println(storage.getInetSocketAddress());

//        ServerInfo[] groups = getServerInfo("group1", "M00/00/00/CrFJKGAyIjuAL2wQAACVfPJeN6Y575.jpg");
//        System.out.println(groups.length);
//        System.out.println(groups[0].getIpAddr());
//        System.out.println(groups[0].getPort());

        System.out.println(getTrackerInfo());
    }
}
