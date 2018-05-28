package com.guy.util;

import com.guy.common.utils.Global;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 邮件管理器
 * java 实现邮件的发送， 抄送及多附件
 * @author zhuxiongxian
 * @version 1.0
 * @created at 2016年10月8日 下午3:52:11
 */
public class EmailManager {

    private  String username; // 服务邮箱(from邮箱)
    private String password ; // 邮箱密码
    private String senderNick;   // 发件人昵称

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSenderNick() {
        return senderNick;
    }

    public void setSenderNick(String senderNick) {
        this.senderNick = senderNick;
    }

    private Properties props; // 系统属性 
    private Session session; // 邮件会话对象 
    private MimeMessage mimeMsg; // MIME邮件对象 
    private Multipart mp;   // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象 

    private static EmailManager instance = null; 

    public EmailManager() {
        props = System.getProperties();
        props.put("mail.smtp.auth", Global.getConfig("ifAuth"));
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", Global.getConfig("smtpServer"));
        props.put("mail.smtp.port", "25");

        // 建立会话
        session = Session.getDefaultInstance(props);
        session.setDebug(false);
    }

    public static EmailManager getInstance() {
        if (instance == null) {
            instance = new EmailManager();
        }
        return instance; 
    }

    /**
     * 发送邮件
     * @param from 发件人
     * @param to 收件人
     * @param copyto 抄送
     * @param subject 主题
     * @param content 内容
     * @param fileList 附件列表
     * @return
     */
    public boolean sendMail(String from, String[] to, String[] copyto, String subject, String content, String[] fileList) {
        boolean success = true;
        try {
            mimeMsg = new MimeMessage(session);
            mp = new MimeMultipart(); 

            // 自定义发件人昵称
            String nick = "";
            try {
                nick = MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 设置发件人
//          mimeMsg.setFrom(new InternetAddress(from));
            mimeMsg.setFrom(new InternetAddress(from, nick));
            // 设置收件人
            if (to != null && to.length > 0) {
                String toListStr = getMailList(to);
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toListStr));
            }
            // 设置抄送人
            if (copyto != null && copyto.length > 0) {
                String ccListStr = getMailList(copyto);
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccListStr)); 
            }
            // 设置主题
            mimeMsg.setSubject(subject);
            // 设置正文
            BodyPart bp = new MimeBodyPart(); 
            bp.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            // 设置附件
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileList[i]); 
                    bp.setDataHandler(new DataHandler(fds)); 
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp); 
                }
            }
            mimeMsg.setContent(mp); 
            mimeMsg.saveChanges(); 
            // 发送邮件
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp"); 
                transport.connect((String)props.get("mail.smtp.host"), username, password);
//              transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO)); 
//              transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close(); 
            } else {
                Transport.send(mimeMsg);
            }
            //保存邮件到发件箱
            saveEmailToSentMailFolder(mimeMsg);
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * 发送邮件
     * @param from 发件人
     * @param to 收件人, 多个Email以英文逗号分隔
     * @param cc 抄送, 多个Email以英文逗号分隔
     * @param subject 主题
     * @param content 内容
     * @param fileList 附件列表
     * @return
     */
    public boolean sendMail(String from, String to, String cc, String subject, String content, String[] fileList) {
        boolean success = true;
        try {
            mimeMsg = new MimeMessage(session);
            mp = new MimeMultipart(); 

            // 自定义发件人昵称
            String nick = "";
            try {
                nick = MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 设置发件人
//          mimeMsg.setFrom(new InternetAddress(from));
            mimeMsg.setFrom(new InternetAddress(from, nick));
            // 设置收件人
            if (to != null && to.length() > 0) {
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            // 设置抄送人
            if (cc != null && cc.length() > 0) {
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc)); 
            }
            // 设置主题
            mimeMsg.setSubject(subject);
            // 设置正文
            BodyPart bp = new MimeBodyPart(); 
            bp.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            // 设置附件
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileList[i]); 
                    bp.setDataHandler(new DataHandler(fds)); 
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp); 
                }
            }
            mimeMsg.setContent(mp); 
            mimeMsg.saveChanges(); 
            // 发送邮件
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp"); 
                transport.connect((String)props.get("mail.smtp.host"), username, password);
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close(); 
            } else {
                Transport.send(mimeMsg);
            }
            //保存邮件到发件箱
            saveEmailToSentMailFolder(mimeMsg);
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public String getMailList(String[] mailArray) {
        StringBuffer toList = new StringBuffer();
        int length = mailArray.length;
        if (mailArray != null && length < 2) {
            toList.append(mailArray[0]);
        } else {
            for (int i = 0; i < length; i++) {
                toList.append(mailArray[i]);
                if (i != (length - 1)) {
                    toList.append(",");
                }

            }
        }
        return toList.toString();
    }


    /**
     * 获取用户的发件箱文件夹
     *
     * @param message
     *            信息
     * @param store
     *            存储
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    private Folder getSentMailFolder(Message message, Store store)
            throws IOException, MessagingException {
               // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", Global.getConfig("imapServer"));
        props.setProperty("mail.imap.port", "143");

//        /** QQ邮箱需要建立ssl连接 */
//        props.setProperty("mail.imap.socketFactory.class",
//                "javax.net.ssl.SSLSocketFactory");
//        props.setProperty("mail.imap.socketFactory.fallback", "false");
//        props.setProperty("mail.imap.starttls.enable", "true");
//        props.setProperty("mail.imap.socketFactory.port", "993");

        // 创建Session实例对象
        Session session = Session.getInstance(props);
        URLName urln = new URLName("imap", Global.getConfig("imapServer"), 143, null,
                username, password);
        // 创建IMAP协议的Store对象
        store = session.getStore(urln);
        store.connect();

        // 获得发件箱
        Folder folder = store.getFolder("Sent Messages");
        // 以读写模式打开发件箱
        folder.open(Folder.READ_WRITE);

        return folder;
    }
    /**
     * 保存邮件到发件箱
     *
     * @param message
     *            邮件信息
     */
    private void saveEmailToSentMailFolder(Message message) {

        Store store = null;
        Folder sentFolder = null;
        try {
            sentFolder = getSentMailFolder(message, store);
            message.setFlag(Flags.Flag.SEEN, true); // 设置已读标志
            sentFolder.appendMessages(new Message[] { message });
            System.out.println("已保存到发件箱...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // 判断发件文件夹是否打开如果打开则将其关闭
            if (sentFolder != null && sentFolder.isOpen()) {
                try {
                    sentFolder.close(true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            // 判断邮箱存储是否打开如果打开则将其关闭
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static void main(String[] args) {
//        String from = username;
//        String[] to = {"10086@qq.com", "xx@zhuxiongxian.cc"};
//        String[] copyto = {"123456@163.com"};
//        String subject = "测试一下";
//        String content = "这是邮件内容，仅仅是测试，不需要回复.";
//        String[] fileList = new String[3];
//        fileList[0] = "d:/zxing.png";
//        fileList[1] = "d:/urls.txt";
//        fileList[2] = "d:/surname.txt";
//        EmailManager.getInstance().sendMail(from, to, copyto, subject, content, fileList);
//    }
}