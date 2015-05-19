# JavaDoc Hints #

  * JavaDoc comments are written in HTML
  * To force a linebreak, use "`<br>`". Example:
> Using "`<br>`":
```
 /**
 * Line 1<br>
 * Line 2
 */
```
> will result in:<br><br>
<blockquote><font color='blue'>Line 1</font><br>
<font color='blue'>Line 2</font><br><br>
</blockquote><blockquote>Whereas not using "<code>&lt;br&gt;</code>":<br>
<pre><code> /**<br>
 * Line 1<br>
 * Line 2<br>
 */<br>
</code></pre>
will result in:<br><br>
<blockquote><font color='blue'>Line 1 Line 2</font>