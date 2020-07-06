CRUD operations on a OneToMany relationship with a java Set type with Eager fetch, backward navigability on that relationship.<br/>
An instance of A has a Map of B.<br/>
An instance of B has a reference to the A instance it is related to.<br/>
The keys of that map is a primitive type (an int) and they are a part of B.
A <><--0..*-----1-- B<br/>
<br/>
compile & execute :<br/>
mvn spring-boot:run<br/>
compile into fat jar then execute :<br/>
mvn clean package<br/>
java -jar target/oneToManyRelationshipMapKeyPrimitiveAndPartOfMapValueEagerWithBackwardNavigability-0.0.1-SNAPSHOT.jar<br/>
<br/>
To Compile from within Eclipse or any other IDE, you need to install Lombok : https://projectlombok.org/setup/overview<br/>
<br/>
<br/>
During the execution, the console shows : <br/>
===== Persisting As and Bs<br/>
===== As<br/>
A{id=1, myString='myString1' Bs : b2 b1 }<br/>
A{id=2, myString='myString2' Bs : b3 b4 }<br/>
A{id=7, myString='myString3' Bs : }<br/>
===== Bs<br/>
B{id=3, b=b1, a.myString=myString1}<br/>
B{id=4, b=b2, a.myString=myString1}<br/>
B{id=5, b=b3, a.myString=myString2}<br/>
B{id=6, b=b4, a.myString=myString2}<br/>
===== Modifying some As and Bs<br/>
===== As<br/>
A{id=1, myString='myModifiedString1' Bs : b2 b4 }<br/>
A{id=2, myString='myModifiedString2' Bs : b3 b1.1 }<br/>
A{id=7, myString='myString3' Bs : }<br/>
===== Bs<br/>
B{id=3, b=b1.1, a.myString=myModifiedString2}<br/>
B{id=4, b=b2, a.myString=myModifiedString1}<br/>
B{id=5, b=b3, a.myString=myModifiedString2}<br/>
B{id=6, b=b4, a.myString=myModifiedString1}<br/>
===== Deleting some As and Bs<br/>
===== As<br/>
A{id=2, myString='myModifiedString2' Bs : b1.1 }<br/>
A{id=7, myString='myString3' Bs : }<br/>
===== Bs<br/>
B{id=3, b=b1.1, a.myString=myModifiedString2}<br/>

--A.java (entity that holds a collection of B entities)<br/>
private String myString;<br/>
@OneToMany(mappedBy="a", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)<br/>
@MapKey(name="b")<br/>
public Map&lt;String, B&gt; bMap;<br/>
we use <b>orphanRemoval = true</b> so a B will be removed from the database when it is removed from its corresponding A's collection.<br/>

--B.java (entity related to a A entity)<br/>
private String b;<br/>
<b>@ManyToOne<br/>
private A a;</b><br/>
a is a reference to the A instance that holds the B instance. It allows backward navigability (we can write b.getA() to retrieve the A instance holding this B instance).<br/>

--ARepository.java<br/>
public List&lt;A&gt; findByMyString(String myString);<br/>
--BRepository.java<br/>
List&lt;B&gt; findByB(String b);<br/>

--AccessingDataJpaApplication.java (main class)<br/>
log.info("===== Persisting As and Bs");<br/>
<b>persistData</b>(aRepository, bRepository);<br/>
readData(aRepository, bRepository);<br/>
log.info("===== Modifying some As and Bs");<br/>
<b>modifyData</b>(aRepository, bRepository);<br/>
readData(aRepository, bRepository);<br/>
log.info("===== Deleting some As and Bs");<br/>
<b>deleteData</b>(aRepository, bRepository);<br/>
readData(aRepository, bRepository);<br/>
...<br/>
<b>persistData(){</b><br/>
&nbsp;&nbsp;//we build A without nested Bs, we set A to each B<br/>
&nbsp;&nbsp;A a1 = new A("myString1");<br/>
&nbsp;&nbsp;A a2 = new A("myString2");<br/>
&nbsp;&nbsp;aRepository.save(a1);<br/>
&nbsp;&nbsp;aRepository.save(a2);<br/>
&nbsp;&nbsp;bRepository.save(new B("b1", a1));<br/>
&nbsp;&nbsp;bRepository.save(new B("b2", a1));<br/>
&nbsp;&nbsp;bRepository.save(new B("b3", a2));<br/>
&nbsp;&nbsp;bRepository.save(new B("b4", a2));<br/>
&nbsp;&nbsp;//we can build an A without Bs<br/>
&nbsp;&nbsp;A a3 = new A("myString3");<br/>
&nbsp;&nbsp;aRepository.save(a3);<br/>
}<br/>
<b>modifyData(){</b><br/>
&nbsp;&nbsp;//we change a1.myString and a2.myString and we affect b1 previously affect at a1 to a2 while changing its b, and move b4 from a2 to a1<br/>
&nbsp;&nbsp;A a1 = aRepository.findByMyString("myString1").get(0);<br/>
&nbsp;&nbsp;A a2 = aRepository.findByMyString("myString2").get(0);<br/>
&nbsp;&nbsp;a1.setMyString("myModifiedString1");<br/>
&nbsp;&nbsp;a2.setMyString("myModifiedString2");<br/>
&nbsp;&nbsp;a2.getBMap().get("b4").setA(a1);<br/>
&nbsp;&nbsp;B b = bRepository.findByB("b1").get(0);<br/>
&nbsp;&nbsp;b.setB("b1.1");<br/>
&nbsp;&nbsp;b.setA(a2);<br/>
&nbsp;&nbsp;aRepository.save(a1);<br/>
&nbsp;&nbsp;aRepository.save(a2);<br/>
&nbsp;&nbsp;bRepository.save(b);<br/>
}<br/>
<b>deleteData(){</b><br/>
&nbsp;&nbsp;//we delete 1 A and 1 B related to another A<br/>
&nbsp;&nbsp;A a1 = aRepository.findByMyString("myModifiedString1").get(0);<br/>
&nbsp;&nbsp;aRepository.delete(a1);<br/>
&nbsp;&nbsp;//we do not directly delete the B instance -> we want to remove that B instance from the A's list<br/>
&nbsp;&nbsp;A a2 = aRepository.findByMyString("myModifiedString2").get(0);<br/>
&nbsp;&nbsp;a2.getBMap().remove("b3");<br/>
&nbsp;&nbsp;aRepository.save(a2);<br/>
}<br/>