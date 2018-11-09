// PersonManager.aidl
package com.lxc.keepalive;

// Declare any non-default types here with import statements
import com.lxc.keepalive.bean.Person;

interface PersonManager {

    int add(in int x, in int y);

    void addPerson(in Person person);

    List<Person> getPersonList();
}
