Runes-of-Wizardry
=================

Runes of Wizardry is a magic mod for Minecraft that allows the player to place patterns of arcane dust to form runes with various effects.

Issue Reporting
----------------
If you wish to report an issue with the mod, please submit one to the issue tracker in this repository.  When creating an 
issue, please be sure to include...

-the version of Runes of Wizardry you are using<br />
-the version of Forge you are using<br />
-the crash report (preferrably via <a href="http://pastebin.com/">Pastebin</a>) generated by the error<br />
-depending on the error, an image of the problem<br />

Contributing
-------------
Runes of Wizardry is open-source under the GPL v3 license.  As a result, you may contribute to the development of the mod via pull requests.

If you plan to contribute, it might be helpful to let others know what you are working on by writing an issue

Suggested setup instructions for making changes:
Note: Advanced instructions and help can be found on the <a href=http://www.minecraftforge.net/forum/index.php/topic,14048.0.html>Minecraft Forge forums</a>

Eclipse:

1. Clone the repository
2. Download the appropriate (currently 1.8-11.14.3.1450) Minecraft Forge source release from the <a href=http://files.minecraftforge.net>Official site</a> (Src link)
3. Copy the 'eclipse' folder from the downloaded archive to the same folder where you cloned the repository. It is a pre-configured workspace that makes the setup much easier.
4. Open a command prompt in the repository folder.
5. Run the commands 'gradlew setupDecompWorkspace' (this will take a while) and 'gradlew eclipse'.
6. Open eclipse and choose the 'eclipse' folder that you copied earlier as the workspace.
7. Done!

IntelliJ IDEA (not tested for the MC 1.8 version):

1. Clone the repository onto your local system.
2. Open a command prompt from the repository folder.
3. Run the commands 'gradlew setupDecompWorkspace' (will take a while) and 'gradlew idea'.
4. Open IntelliJ and point to either the project folder or the build.gradle file.
5. After opening the project in IntelliJ, run 'gradlew genIntellijRuns' in the command prompt you opened earlier.
6. You now have a functional local copy of Runes of Wizardry, ready to develop on.

NetBeans (not tested for the MC 1.8 version):

1. Install the gradle plugin for NetBeans
2. Go to Tools->Settings->Misc->gradle and change "build script evaluation" to "Idea based [...]"
3. Open a command prompt from the repository folder.
4. Run the commands 'gradlew setupDecompWorkspace' (will take a while) and 'gradlew idea'.
4. Open the project folder in NetBeans
5. In "project properties", go to "manage built-in tasks"
6. In the "run" task, uncheck inherited and change run to runClient
7. Done!


Development Team/Credits
-------------------------

Xilef11- Current maintainer<br/>
zombiepig333/lightningpig333- Project founder<br/>
billythegoat101 - Original author of <a href=http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1280442-1-5-the-runic-dust-mod-mar14>The Runic Dust Mod</a>
