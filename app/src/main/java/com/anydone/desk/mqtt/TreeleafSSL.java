package com.anydone.desk.mqtt;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.common.collect.Maps;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.FileUtils;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.TreeleafException;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jcajce.util.NamedJcaJceHelper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;

/**
 * Created by Dipak Malla
 * Email dpakmalla@gmail.com
 * Created on 2/15/19.
 */
public class TreeleafSSL {
    private static final String TAG = "TreeleafSSL";
    private static final String TLS_VERSION = Constants.TLS_VERSION;
    private static final boolean SELF_SIGNED = true;
    private static final JcaJceHelper helper;
    private static final Map<Object, String> algorithms = Maps.newHashMapWithExpectedSize(3);

    static {
        algorithms.put(X9ObjectIdentifiers.id_ecPublicKey, "ECDSA");
        algorithms.put(PKCSObjectIdentifiers.rsaEncryption, "RSA");
        algorithms.put(X9ObjectIdentifiers.id_dsa, "DSA");
        helper = new NamedJcaJceHelper("BC");
    }

    private TreeleafSSL() {

    }

    private static X509Certificate getCertificate(final InputStream fileInputStream)
            throws TreeleafException {
//        InputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
//            fileInputStream = new FileInputStream(caCrtFile);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            if (bufferedInputStream.available() > 0) {
                return (X509Certificate) cf.generateCertificate(bufferedInputStream);
            }
        } catch (Exception e) {
            throw new TreeleafException(e);
        } finally {
            FileUtils.close(fileInputStream);
            FileUtils.close(bufferedInputStream);
        }
        throw new TreeleafException("Could not certificate.");
    }

    private static KeyFactory getKeyFactory(AlgorithmIdentifier algId)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        ASN1ObjectIdentifier algorithm = algId.getAlgorithm();
        String algName = algorithms.get(algorithm);
        if (algName == null) {
            algName = algorithm.getId();
        }
        try {
            return helper.createKeyFactory(algName);
        } catch (NoSuchAlgorithmException e) {
            if (algName.equals("ECDSA")) {
                return helper.createKeyFactory("EC");
            }
            throw e;
        }
    }

    private static KeyPair loadPrivateKey(final InputStream keyFile,
                                          final String password,
                                          byte[] pubKey) throws TreeleafException, IOException {

        File clientKeyFile = FileUtils.generateFileFromInputStream(keyFile);

        try (
                PEMParser pemParser = new PEMParser(new FileReader(clientKeyFile))) {
            Object object = pemParser.readObject();
            KeyPair key;
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            if (object instanceof PEMEncryptedKeyPair) {
                if (null == password) {
                    throw new TreeleafException("Key password cannot be null.");
                }
                PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder()
                        .build(password.toCharArray());
                GlobalUtils.showLog(TAG, "Key opened with given password");
                key = converter.getKeyPair(((PEMEncryptedKeyPair) object)
                        .decryptKeyPair(decProv));
            } else if (object instanceof PrivateKeyInfo) {
                PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) object;
                KeyFactory keyFactory = getKeyFactory(privateKeyInfo.getPrivateKeyAlgorithm());
                key = new KeyPair(keyFactory.generatePublic(new X509EncodedKeySpec(pubKey)),
                        keyFactory.generatePrivate(
                                new PKCS8EncodedKeySpec(privateKeyInfo.getEncoded())));
            } else {
                key = converter.getKeyPair((PEMKeyPair) object);
            }
            return key;
        } catch (
                Exception ex) {
            throw new TreeleafException(ex);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static SSLSocketFactory getSocketFactory(final InputStream caCrtFile,
                                                    final InputStream crtFile,
                                                    final InputStream keyFile,
                                                    final String password) throws TreeleafException {
        if (null == caCrtFile) {
            throw new TreeleafException("CA certificate is null.");
        }
        if (null == crtFile) {
            throw new TreeleafException("Client certificate is null.");
        }
        if (null == keyFile) {
            throw new TreeleafException("Key file is null.");
        }
        if (null == password) {
            throw new TreeleafException("Key password is required.");
        }
        Security.addProvider(new BouncyCastleProvider());
        //Load CA certificate
        GlobalUtils.showLog(TAG, "Loading CA certificate from : {}" + caCrtFile);
        X509Certificate caCert = getCertificate(caCrtFile);
        //Load client certificate
        GlobalUtils.showLog(TAG, "Loading client certificate from : {}" + crtFile);
        X509Certificate cert = getCertificate(crtFile);
        //Load private key
        try {
            KeyPair key = loadPrivateKey(keyFile, password, cert.getPublicKey().getEncoded());
            // CA certificate is used to authenticate server
            KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
            caKs.load(null, null);
            caKs.setCertificateEntry("ca-certificate", caCert);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(caKs);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            ks.setCertificateEntry("certificate", cert);
            ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),
                    new java.security.cert.Certificate[]{cert});
            KeyManagerFactory kmf = KeyManagerFactory.getInstance
                    (KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password.toCharArray());
            SSLContext context = SSLContext.getInstance(TLS_VERSION);
            context.init(kmf.getKeyManagers(),
                    SELF_SIGNED ? getTrustManager() : tmf.getTrustManagers(),
                    new SecureRandom());
            return context.getSocketFactory();
        } catch (Exception ex) {
            throw new TreeleafException(ex);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static TrustManager[] getTrustManager() {
        return new TrustManager[]{new X509ExtendedTrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s,
                                           Socket socket) throws CertificateException {
                //Empty on purpose
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates,
                                           String s, Socket socket) throws CertificateException {
                //Empty on purpose
            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s,
                                           SSLEngine sslEngine) throws CertificateException {
                //Empty on purpose
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s,
                                           SSLEngine sslEngine) throws CertificateException {
                //Empty on purpose
            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                    throws CertificateException {
                //Empty on purpose
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                    throws CertificateException {
                //Empty on purpose
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
    }
}
