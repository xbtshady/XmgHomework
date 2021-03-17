package org.geektimes.projects.user.management;

public class MyManager implements MyManagerMBean{

    public  String appName;
    @Override
    public String getAppName() {
        return appName;
    }
}
