// PersonManager.aidl
package com.lxc.keepalive;

// Declare any non-default types here with import statements
import com.lxc.keepalive.bean.Person;

interface PersonManager {

    // 除了基本数据类型，其他类型的参数都需要标上方向类型：in(输入), out(输出), inout(输入输出)
    // in表明是由客户端到服务端
    // out表明是由服务端到客户端
    // inout表明可以双向通信
    // 默认是in这种方式，所以如果要用到后面的两种就需要在实体类中使用readFromParcel方法读取数据

    int add(in int x, in int y);

    void addPerson(in Person person);

    List<Person> getPersonList();
}
