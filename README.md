*The version published on the due date (19 April 2023) has been tagged [v0.1.2-due-date](https://github.com/rustam-isangulov/HorizonDiscoveryTest/tree/v0.1.2-due-date).*

# Horizon Discovery – Technical Test

## Objective

> *Write an application (console is fine) that outputs a ranked list of URLs along with how many "hits" each has got. Do use artistic license but document any assumptions you make.*

## Run the application on example logs using precompiled jars

### System requirements
Java 11
#### tested environments
macos:
```shell
java -version
openjdk version "11.0.12" 2021-07-20
OpenJDK Runtime Environment Homebrew (build 11.0.12+0)
OpenJDK 64-Bit Server VM Homebrew (build 11.0.12+0, mixed mode)
```
### Steps
1. clone this repository

```shell
git clone https://github.com/rustam-isangulov/HorizonDiscoveryTest.git
```
2. change directory to `HorizonDiscoveryTest`

```shell
cd HorizonDiscoveryTest
```
3. run the application to output aggregated *W3C* log data to console

```shell
java -jar bin/LogProcessor.jar -p W3C -f test_logs/W3CLog.txt test_logs/W3CLog1.txt
```

<details><summary>expected output</summary>
<p>

```shell
[18, /images/picture.jpg, 2002-05-04, 17:42:22]
[12, /images/cartoon.gif, 2002-05-04, 17:42:25]
[6, /images/text.txt, 2002-05-03, 17:42:25]
```
</p>
</details>

4. run the application to output aggregated *NCSA* log data to console

```shell
java -jar bin/LogProcessor.jar -p NCSA -f test_logs/NCSALog.txt test_logs/NCSALog1.txt
```

<details><summary>expected output</summary>
<p>

```shell
[18, /images/picture.jpg, 2002-05-04 17:42:22, 3256]
[12, /images/cartoon.gif, 2002-05-04 17:42:25, 3256]
[6, /images/text.txt, 2002-05-03 17:42:25, 3256]
```
</p>
</details>

## Assumptions and references
- Reading data from files is the primary input mechanism
- Writing data should support at least console and files
- Supported formats should at least include W3C (dynamic columns) and NCSA (fixed columns)
- W3C files format reference: [source](https://www.w3.org/TR/WD-logfile)
- NCSA files format reference: [source](https://learn.microsoft.com/en-us/windows/win32/http/ncsa-logging)
- No particular aggregation format was chosen, a placeholder `formatter` is the default `List<String>::toString()` method call

## Partial UML diagrams

![Process](./HDTLPTop.jpeg)
![Configuration](./HDTLPConfiguration.jpeg)
