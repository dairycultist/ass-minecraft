import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Gdx;

import java.io.BufferedReader;
import java.io.IOException;

public class ModLoader {

    public interface ModFileReader {

        void run(String namespace, String line, String modPath) throws IOException;
    }

    // TODO a helper function that takes a lambda expression
    // ignores comments

    /** Reads over every single mod file line by line. */
    public static void read(ModFileReader lambda) {

        for (FileHandle modDir : Gdx.files.local("mods/").list()) {

            if (modDir.isDirectory()) {

                FileHandle modFile = modDir.child("mod.txt");

                if (!modFile.exists()) {

                    System.out.println("Warning: The mod folder \"" + modDir.name() + "\" doesn't have a mod.txt configuration file!");
                    continue;
                }

                BufferedReader reader = modFile.reader(1024);

                try {

                    String namespace = reader.readLine().trim();

                    if (!namespace.startsWith("@") || namespace.contains(" ")) {

                        System.out.println("Warning: The mod file in the mod folder \"" + modDir.name() + "\" has an invalid namespace! (File MUST start with @<namespace> where namespace has no internal whitespace)");
                        continue;
                    }

                    namespace = namespace.substring(1);

                    String line;

                    while ((line = reader.readLine()) != null) {

                        line = line.trim();

                        if (line.startsWith("//"))
                            continue;
                        
                        if (line.isEmpty())
                            continue;
                        
                        lambda.run(namespace, line, "mods/" + modDir.name() + "/");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
