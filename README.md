<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="https://stackedit.io/style.css" />
</head>

<body class="stackedit">
  <div class="stackedit__html"><p>To get a Git project into your build:</p>
<p>Step 1. Add the JitPack repository to your build file<br>
Add it in your root build.gradle at the end of repositories:</p>
<pre><code>allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
</code></pre>
<p>Step 2. Add the dependency</p>
<pre><code>dependencies {
	         implementation 'com.github.Iaovy:Base-Frame:v1.0.0'
	}
</code></pre>
<p>Android快速开发框架<br>
复写Activity、Fragment和RecylerViewAdapter帮助您快速UI开发，请继承Base系列<br>
复写RxJava+Retrofit+OkHttp配合多种数据处理方式，让网络请求变的更简单<br>
逻辑分页请使用PageHelper，物理分页请使用WLPage<br>
语音、定位和时间初始化等更多动能请自行探索<br>
感谢使用</p>
</div>
</body>

</html>


