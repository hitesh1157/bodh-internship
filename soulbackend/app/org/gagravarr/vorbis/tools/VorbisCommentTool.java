/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gagravarr.vorbis.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.gagravarr.vorbis.VorbisAudioData;
import org.gagravarr.vorbis.VorbisFile;
import org.gagravarr.vorbis.tools.VorbisLikeCommentTool.Command.Commands;

/**
 * A class for listing and editing Comments
 *  within a Vorbis File, much like the
 *  vorbiscomment program.
 */
public class VorbisCommentTool extends VorbisLikeCommentTool {
    public static void main(String[] args) throws Exception {
        Command command = processArgs(args, "VorbisComment");
        
        VorbisFile vf = new VorbisFile(new File(command.inFile));
        
        if (command.command == Commands.List) {
            listTags(vf.getComment());
        } else {
            // Have the new tags added
            addTags(vf.getComment(), command);
            
            // Write out
            List<VorbisAudioData> audio = new ArrayList<VorbisAudioData>();
            VorbisAudioData ad;
            while( (ad = vf.getNextAudioPacket()) != null ) {
                audio.add(ad);
            }

            // Now write out
            vf.close();
            VorbisFile out = new VorbisFile(
                    new FileOutputStream(command.outFile),
                    vf.getSid(),
                    vf.getInfo(),
                    vf.getComment(),
                    vf.getSetup()
            );
            for(VorbisAudioData vad : audio) {
                out.writeAudioData(vad);
            }
            out.close();
        }
    }
}