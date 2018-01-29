package crstv.app.com.crstv;

import java.io.Serializable;

/**
 * Created by eutiquio on 12/8/17.
 */

public class Datos implements Serializable {
    private int id;
    private String group;
    private String logo;
    private String name;
    private String channel;
    private String list;

    public Datos(int id, String group, String logo, String name, String channel) {
        this.id = id;
        this.group = group;
        this.logo = logo;
        this.name = name;
        this.channel = channel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }
}
