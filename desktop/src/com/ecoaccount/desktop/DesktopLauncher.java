package com.ecoaccount.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ecoaccount.Main;
import com.ecoaccount.Platform;

public class DesktopLauncher {

	private static Platform platform = new Platform() {
		@Override
		public void showScanner() {
//			System.out.println("showScanner");
		}
		@Override
		public void hideScanner() {
//			System.out.println("hideScanner");
		}
		@Override
		public void setAutofocus(boolean focus) {
//			System.out.println("setAutofocus");
		}
		@Override
		public void setFlashLight(boolean flash) {
//			System.out.println("setFlashLight");
		}

		@Override
		public void setScannerBounds(int x, int y, int width, int height) {
//			System.out.println("setScannerBounds");

		}
	};


	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.height = (int) (LwjglApplicationConfiguration.getDesktopDisplayMode().height * 0.9f);
//		config.width = config.height / 16 * 9;
//		config.fullscreen = true;

		config.width = config.height / 4 * 3;
//		config.width = config.height / 20 * 9;
		config.initialBackgroundColor = Main.clBackground;
		config.title = "EcoAccount";
		config.addIcon("images/icon_logo32.png", Files.FileType.Internal);
		config.resizable = false;
		config.y = 0;

		new LwjglApplication(new Main(platform), config);
	}
}
