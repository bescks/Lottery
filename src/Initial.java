import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.MessageDigest;
import java.util.logging.Logger;

public class Initial {
    private Logger logger;
    private String mac;

    Initial(Logger log) {
        logger = log;
    }

    boolean isReady() {
        mac = getMac();
        if (mac.toLowerCase().contains("error".toLowerCase())) {
            logger.severe("MAC address : " + mac);
            return false;
        }
        logger.info("MAC address : " + mac);

        String url = "https://raw.githubusercontent.com/skipAll/macsTable/master/macsDatabase.txt";
        String macsTable = getHTML(url);
        if (macsTable.toLowerCase().contains("error".toLowerCase())) {
            logger.severe(macsTable);
            return false;
        }
        logger.info("Read macsDatabase successfully!");

        String cert = getCert();
        if (cert.equals("CertificateIsValid")) {
            logger.info("Check certificate successfully!");
            if (macsTable.toLowerCase().contains(mac.toLowerCase())) {
                logger.info("Certificate is blocked!");
                return false;
            } else {
                return true;
            }
        } else if (cert.equals("CertificateIsInvalid")) {
            logger.info("Check certificate failed!");
            return false;
        } else if (cert.toLowerCase().contains("error".toLowerCase())) {
            logger.severe(cert);
            return false;
        } else {
            logger.severe("unknown reason!");
            return false;
        }

    }

    private String getMac() {
        try {
            InetAddress ip;
            ip = InetAddress.getLocalHost();
            //  System.out.println("IP address : " + ip.getHostAddress());
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] hwAddr = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hwAddr.length; i++) {
                sb.append(String.format("%02X%s", hwAddr[i], (i < hwAddr.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (Exception e) {
            return "errorFromGetMac[" + e + "]";
        }
    }

    private String getHTML(String urlToRead) {
        try {
            StringBuilder sb = new StringBuilder();
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            rd.close();
            return sb.toString();
        } catch (Exception e) {
            return "errorFromHttp[" + e + "]";
        }
    }

    private String Md(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            return "errorFromMd[" + input + "][" + e + "] ";
        }


    }

    private String renewCert() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("证书.txt"), "utf-8"))) {
            writer.write("用户名：" + mac + "\n\r密码：");
            return "errorFromCert[Initiate a new cert file!]";
        } catch (Exception e) {
            return "errorFromCert[e1:" + e + "]";
        }

    }

    private String getCert() {
        String result;
        try {
            if (!new File("证书.txt").isFile()) {
                return renewCert();
            } else {
                BufferedReader br = new BufferedReader(new FileReader("证书.txt"));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                if (!sb.toString().toLowerCase().contains(mac.toLowerCase())) {
                    return renewCert() + "[Because two mac doesn't match!]";
                }
                result = sb.toString();
            }
        } catch (Exception e) {
            return "errorFromCert[e2:" + e + "] ";
        }

        String macEncrypted = getMac() + "-ciao";
        String certificate = Md(macEncrypted);
        if (result.toLowerCase().contains(certificate.toLowerCase())) {
            getHTML("https://api.thingspeak.com/update?api_key=A7EQRIH5PD6IFLH7&field1=" + mac);
            return "CertificateIsValid";
        } else {
            return "CertificateIsInvalid";
        }

    }

}
