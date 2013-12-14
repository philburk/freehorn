package org.frogpeak.horn;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author Phil Burk (C) 2003
 */
public class FreeHorn
{
	public static final String VERSION = "0.4 (build 17)";
	private HornView view;
	HornModel model;
	HornGUI gui;
	HornPlayer player;

	public static void main(String[] args)
	{
		System.out.println("freeHorn by Larry Polansky");
		System.out.println("Today is " + new Date());

		FreeHorn horn = new FreeHorn();
		horn.setup();
	}

	/**
	 * 
	 */
	private void setup()
	{
		model = new HornModel();
		player = new HornPlayer(model);

		gui = new HornGUI(this, model, player);
		gui.show();

		HornSynth.getInstance().start( model.getMaxPolyphony() );
		
		view = new HornView(this, model, player);
		view.setBounds(ScreenLayout.viewRect);
		view.show();
	}

	public void quit()
	{
		HornSynth.getInstance().stop();
		System.exit(0);
	}

	public void updateGUI()
	{
		if (gui != null)
		{
			gui.hide();
		}
		gui = new HornGUI(this, model, player);
		gui.show();
	}

	public void openPiece()
	{
		savePiece();
		File file =
			FileChooser.browseLoad(
				gui,
				"Select Project File",
				model.getSaveFile(),
				".properties");
		if (file != null)
		{
			try
			{
				model.load(file);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			updateGUI();
		}
	}

	public void savePiece()
	{
		try
		{
			model.save();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void savePieceAs()
	{
		File file =
			FileChooser.browseSave(gui, "Save As...", model.getSaveFile());
		String name = file.getAbsolutePath();
		if( !name.endsWith(".properties"))
		{
			name += ".properties";
			file = new File(name);
		}
		if (file != null)
		{
			try
			{
				model.saveAs(file);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
