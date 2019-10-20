package com.liupeidong.kafkaCore.constant;

import com.liupeidong.kafkaCore.util.PropertiesFile;

/**
 * Config class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 16:27
 */
public class Config {

    private PropertiesFile props;
    private int totalCount;
    private String bootstrap_servers;
    private String zookeeper_connector;
    private String request_required_acks;
    private int produce_thread_count;
    private int send_thread_count;
    private int initialCount;
    private String topic;
    private boolean print_progress;
    private String print_progress_strategy;
    private int print_progress_interval;
    private String msg_generate_class;


    public Config(PropertiesFile props) {
        this.props = props;
        this.totalCount = props.getIntProperty("totalCount", 0);
        this.bootstrap_servers = props.getStringProperty("bootstrap.servers");
        this.zookeeper_connector = props.getStringProperty("zookeeper.connector");
        this.request_required_acks = props.getStringProperty("request.required.acks", "1");
        this.produce_thread_count = props.getIntProperty("produce_thread_count", 0);
        this.send_thread_count = props.getIntProperty("send_thread_count", 0);
        this.initialCount = props.getIntProperty("initialCount", 0);
        this.topic = props.getStringProperty("topic");
        this.print_progress = props.getBooleanProperty("print_progress", true);
        this.print_progress_strategy = props.getStringProperty("print_progress_strategy");
        this.print_progress_interval = props.getIntProperty("print_progress_interval", 1000);
        this.msg_generate_class = props.getStringProperty("msg_generate_class");



    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public String getBootstrapServers() {
        return bootstrap_servers;
    }

    public String getZookeeperConnector() {
        return zookeeper_connector;
    }

    public String getRequestRequiredAcks() {
        return request_required_acks;
    }

    public Integer getProduceThreadNum() {
        return produce_thread_count;
    }

    public Integer getSendThreadNum() {
        return send_thread_count;
    }

    public Integer getiInitialCount() {
        return initialCount;
    }


    public String getTopic() {
        return topic;
    }

    public String getMsgGenerateClass() {
        return msg_generate_class;
    }



    public boolean printProgress() {
        return print_progress;
    }

    public String getProgressStrategy() {
        return print_progress_strategy;
    }

    public int getProgressInterval() {
        return print_progress_interval;
    }

}
