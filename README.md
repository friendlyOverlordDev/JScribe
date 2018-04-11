# JScribe
... is a simple tool to add texts to graphics.

Get the latest version here: [Releases](https://github.com/friendlyOverlordDev/JScribe/releases)

Developed in Java to allow usage on many different platforms.

Since pictures are usually better than a long text here a few pictures of the program:
![main window](https://raw.githubusercontent.com/friendlyOverlordDev/JScribe/master/screenshot/mainWindow.png)

The main editor for placing as well as editing text and notes.
Notes can be converted to text and neither notes nor borders around text will be displayed when images are exported.

![the style editor](https://raw.githubusercontent.com/friendlyOverlordDev/JScribe/master/screenshot/styleEditor.png)

With the style editor and a tag-based styling system the style of multiple components can be changed easily.


### Changelog
* [0.7.1](https://github.com/friendlyOverlordDev/JScribe/releases/download/0.7.1/JScribe-0.7.1.zip "Download")
	* Images in a project can now be:
		- added
		- replaced
		- renamed
		- copied
		- deleted
	* Changes to styles will update on the fly and not only after closing the window.
* [0.7.0](https://github.com/friendlyOverlordDev/JScribe/releases/download/0.7.0/JScribe-0.7.0.zip "Download")
	* JOrtho was added as a spell checker (currently english only, however it can be expanded if needed)
	* some simple zooming is now possible
	* the styles where expanded with an "all caps" option which renders text in uppercase regardless of how they are written
	* a double click on a text-element will now open the style editor
	* panic button (F1 & F5) added, which will update the Program and should help to fix graphic based issues, when they appear
	* scrolling is reset when a new image is loaded
	* release-jars will be part of the repository
	(probably not the most elegant way, but now those who accidentally download the repository will also have the files)
* [0.6.2 public Beta](https://github.com/friendlyOverlordDev/JScribe/releases/download/0.6.2/JScribe.zip "Download")
	* first release


### Donation
While JScribe is completly free, if you like it and want to support the developer, you can donate:
* Bitcoin (BTC): 1PPDyhR5fcybRtBf8fntwAtRWVCGakkjZR
* Litecoin (LTC): Lcs9kuZNtPEC7qMpawTPPeVLka8XZpM46Z
* Dash: Xg3Z6S4Bn2pLRVfebZR1e94MdnmtmmQZMd


### supported Files:
* __Graphic-Files__ (png, jpg, jpeg, gif)
Files containing basic graphics. JScribe can import and export those.
* __JScribe-Files__ (jsc)
Formated text-files containing notes and texts for graphics.
Since those files link to one or more specific graphics, those graphics shouldn't be moved or renamed.
* __JScribe-Archive__ (jsa)
Zip-Archive containing a jsc file as well as all files linked by the jsc file.
Especially useful if JScribe is used by teams, since it allows an easy transmission of all files.
* __JScribe-Style-Files__ (jss)
Formated text-files containing styling information for texts.
Allows import and export of styling information from and to multiple projects.




### License
JScribe is published under GPLv3.
See [LICENSE](LICENSE) for more infos.


