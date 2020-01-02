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
</div>
</body>

</html>
