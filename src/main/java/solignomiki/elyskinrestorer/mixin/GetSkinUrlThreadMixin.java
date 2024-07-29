package solignomiki.elyskinrestorer.mixin;

import com.b100.utils.StringUtils;
import net.minecraft.core.util.helper.GetSkinUrlThread;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Base64;

@Mixin(value = GetSkinUrlThread.class, remap = false)
public abstract class GetSkinUrlThreadMixin extends Thread {

	@Overwrite()
	public String getSkinObject(String name) {
		String skinObject = "";
		try {
			skinObject = StringUtils.getWebsiteContentAsString("http://skinsystem.ely.by/profile/" + name);
		} catch (Exception e) {
			System.out.println("Ely.by skin not found");
		}
		if (skinObject == "") {
			try {
				String customTexturesString =
					"{\n" +
						"  \"textures\": {\n" +
						"    \"SKIN\": {\n" +
						"      \"url\": \"http://auth.tlauncher.org/skin/fileservice/skins/skin_"+name+".png\",\n" +
						"    }\n" +
						"  }\n" +
						"}";
				customTexturesString = customTexturesString.replace("\n", "").replace("  ", "");

				String skinObjectText =
					"{\n" +
						"  \"properties\": [\n" +
						"    {\n" +
						"      \"name\": \"textures\",\n" +
						"      \"value\": \"" + Base64.getEncoder().encodeToString(customTexturesString.getBytes()) + "\"\n" +
						"    }\n" +
						"  ]\n" +
						"}";
				skinObjectText = skinObjectText.replace("\n", "").replace("  ", "");

				skinObject = skinObjectText;
			} catch (Exception e) {
				System.out.println("TLauncher skin not found");
			}
		}

		return skinObject;
	}
}
