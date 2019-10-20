package com.liupeidong.kafkaCore.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * PropertiesFile class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 16:28
 */
public class PropertiesFile {
    private final static Logger logger = LoggerFactory.getLogger(PropertiesFile.class);
    private Properties prop;
    private String fileName;

    /**
    * description
    * @author liupeidong
    * @date 2019/10/06 16:47
     * @param _fileName
     * @param _outside
     * @return
    **/
    public PropertiesFile(String _fileName, boolean _outside){
        this.prop = new Properties();
        this.fileName = _fileName;

        InputStream inputStream = null;
        try{
            if(_outside){
                inputStream = getInputStreamByFile(_fileName);
            } else{
                inputStream = getInputStream(Thread.currentThread().getContextClassLoader(), _fileName);
                if(inputStream == null){
                    inputStream = getInputStream(PropertiesFile.class.getClassLoader(), _fileName);
                }
            }
            prop.load(inputStream);
        } catch(Exception e){
            logger.error("can not find the config file." + _fileName, e);
            throw new RuntimeException("can not find the config file." + _fileName, e);
        } finally{
            if(inputStream != null){
                try{
                    inputStream.close();
                } catch(IOException ie){
                    logger.error("close the stream failed.", ie);
                }
            }
        }
    }

    public static InputStream getInputStreamByFile(String path) {
        File file = new File(path);
        if (!file.isFile() || !file.exists()) {
            throw new IllegalArgumentException("文件" + path + "不存在");
        }

        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return in;
    }


    public static InputStream getInputStream(ClassLoader _classLoader, String _fileName) {
        if (_classLoader == null || StringUtils.isBlank(_fileName)) {
            logger.info("classLoader is null or fileName is null");
            return null;
        }

        _fileName = _fileName.trim();

        InputStream stream = null;
        try {
            stream = _classLoader.getResourceAsStream(_fileName);
        } catch (Exception e) {
            logger.error("read " + _fileName + " error", e);
        }

        if (stream == null && !_fileName.startsWith("/")) {
            try {
                stream = _classLoader.getResourceAsStream("/" + _fileName);
            } catch (Exception e) {
                logger.error("read /" + _fileName + " error", e);
            }
        }
        return stream;
    }

    public String getStringProperty(String propertyName) {
        return prop.getProperty(propertyName);
    }

    public String getStringProperty(String propertyName, String dft) {
        String value = prop.getProperty(propertyName);
        if (StringUtils.isBlank(value)) {
            return dft;
        }
        return value;
    }

    public Integer getIntProperty(String propertyName, Integer dft) {
        String raw = prop.getProperty(propertyName);
        return getInt(raw, dft);
    }

    public Long getLongProperty(String propertyName, Long dft) {
        String raw = prop.getProperty(propertyName);
        return getLong(raw, dft);
    }

    public Boolean getBooleanProperty(String propertyName, Boolean dft) {
        String raw = prop.getProperty(propertyName);
        return getBoolean(raw, dft);
    }

    /**
     * @param propertyName
     * @param propertyValue
     * @author lihong10 2015-6-15 下午4:16:54
     * @since v1.0
     */
    public void setProperty(String propertyName, String propertyValue) {
        prop.setProperty(propertyName, propertyValue);
    }

    /**
     * @return the Properties
     */
    public Properties getProps() {
        return prop;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    private Integer getInt(String _str, Integer _dft) {
        try {
            return Integer.parseInt(_str.trim());
        } catch (Exception e) {
            logger.error("error when parsing " + _str + " to int, use default value: " + _dft);
            return _dft;
        }
    }

    private Long getLong(String _str, Long _dft) {
        Long value = null;
        try {
            value =  Long.parseLong(_str.trim());
        } catch (Exception e) {
            logger.error("error when parsing " + _str + " to long, use default value: " + _dft);
            return _dft;
        }

        return (value == null) ? _dft : value;
    }

    private Boolean getBoolean(String _str, Boolean _dft) {
        try {
            return Boolean.parseBoolean(_str.trim());
        } catch (Exception e) {
            logger.error("error when parsing " + _str + " to bool, use default value: " + _dft);
            return _dft;
        }
    }
}
