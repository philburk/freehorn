package org.frogpeak.horn;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.softsynth.jsyn.AppletFrame;
import com.softsynth.jsyn.SynthContext;

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
	EQGUI eQ;
	
	TJ_Devices devices;
	
	Point location1;
	Point location2;
	Point location3;

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
		
		devices = new TJ_Devices();
		AppletFrame devicesFrame = new AppletFrame( "Audio Settings", devices );
		devicesFrame.resize( 650, 150 );
		devicesFrame.setLocation(600, 0);
		devicesFrame.show();
		
		model = new HornModel();
		player = new HornPlayer(model);
		
		devices.start(this);
		
		gui = new HornGUI(this, model, player);
		view = new HornView(this, model, player);
		eQ = new EQGUI(this, model, player);
		
		devices.startAudio();
		
		location1 = new Point(0, 0);
		location2 = new Point(600, 175);
		location3 = new Point(600, 480);
		
		gui.setLocation(location1);
		
		//view.setBounds(ScreenLayout.viewRect);
		view.setLocation(location2);
		
		//eQ.setBounds(ScreenLayout.viewRect);
		eQ.setLocation(location3);
		
		gui.pack();
		view.pack();
		eQ.pack();
		
		gui.setVisible(true);
		view.setVisible(true);
		eQ.setVisible(true);
	}
	
	public void finalizeSetup()
	{

		//HornSynth.getInstance().start( model.getMaxPolyphony())
		if(gui != null){
			disposeGui();
		}

		gui = new HornGUI(this, model, player);
		gui.setLocation(location1);
		gui.pack();
		
		view = new HornView(this, model, player);
		//view.setBounds(ScreenLayout.viewRect);
		view.setLocation(location2);
		view.pack();
		
		eQ = new EQGUI(this, model, player);
		//eQ.setBounds(ScreenLayout.viewRect);
		eQ.setLocation(location3);
		eQ.pack();
		
		gui.setVisible(true);
		view.setVisible(true);
		eQ.setVisible(true);

	}
	
	public void disposeGui(){
		location1 = gui.getLocation();
		location2 = view.getLocation();
		location3 = eQ.getLocation();
		System.out.println(gui.getLocation());
		System.out.println(view.getLocation());
		System.out.println(eQ.getLocation());
		gui.dispose();
		view.dispose();
		eQ.dispose();
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
			disposeGui();
		}
		gui = new HornGUI(this, model, player);
		
		view = new HornView(this, model, player);
		//view.setBounds(ScreenLayout.viewRect);
		
		eQ = new EQGUI(this, model, player);
		//eQ.setBounds(ScreenLayout.viewRect);
		
		gui.setLocation(location1);
		gui.pack();

		view.setLocation(location2);
		view.pack();
		eQ.setLocation(location3);
		eQ.pack();
		
		gui.setVisible(true);
		view.setVisible(true);
		eQ.setVisible(true);
		
		System.out.println(gui.getLocation());
		System.out.println(view.getLocation());
		System.out.println(eQ.getLocation());
	}

	public void openPiece()
	{
		//savePiece();
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
