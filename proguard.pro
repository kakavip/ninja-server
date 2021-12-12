
-dontshrink
-dontoptimize
-verbose

-libraryjars 'C:\Program Files\Java\jdk1.8.0_211\jre\lib\rt.jar'

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keepclasseswithmembernames,includedescriptorclasses class *{
 public <init>(*);
}

-keep class *{
 public <init>(*);
}

-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


-keep enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
    *** get*();
    void set*(***);
}


-keep class !boardGame.**, !io.**, !patch.**,!server.**  { *; }


-keep class java.** {*;}
-dontwarn java.**

-keep class lombok.** {*;}
-dontwarn lombok.**

-keep class org.** {*;}
-dontwarn org.**

-keep class com.** {*;}
-dontwarn com.**

-keep class javax.** {*;}
-dontwarn javax.**






-dontnote org.apache.**
-dontnote org.codehaus.**
-dontnote javax.enterprise.**
-dontnote javax.**
-dontnote sun.**
-dontnote org.eclipse.**
-dontnote java.**
-dontnote javax.**
-dontnote com.**
-dontnote org.**
-dontnote lombok.**

-dontwarn *
#-dontobfuscate
# Additionally you will need to keep specific classes. A common use case is keeping all
# of the models that are JSON parsed using something like Jackson.

