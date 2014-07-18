package org.takanolab.ar.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.takanolab.ar.log.LogWriter;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.paar.ch9.MainActivity;
import com.paar.ch9.SearchDataSource;
import org.apache.lucene.analysis.PorterStemmer;
import org.takanolab.ar.db.*;

import jp.androidgroup.nyartoolkit.R;


public class SearchActivity extends ListActivity {
	
	private static List<Document> documents = null;
	
	private static HashMap<String, Integer> thumbIdMap = 
		    new HashMap<String, Integer>() {{
//		    	// Satoi
//		    	put("akanesumire", R.drawable.akanesumire);
//		    	put("anaguma", R.drawable.anaguma);
//		    	put("aosuziageha", R.drawable.aosuziageha);
//		    	put("bald_eagle", R.drawable.bald_eagle);
//		    	put("baltimore_oriole", R.drawable.baltimore_oriole);
//		    	put("bighornsheep", R.drawable.bighornsheep);
//		    	put("black_backed_woodpecker", R.drawable.black_backed_woodpecker);
//		    	put("blackbass", R.drawable.blackbass);
//		    	put("brilliant_blue_discus_fish", R.drawable.brilliant_blue_discus_fish);
//		    	put("broad_winged_hawk", R.drawable.broad_winged_hawk);
//		    	put("brown_trout", R.drawable.brown_trout);
//		    	put("cgs_s", R.drawable.cgs_s);
//		    	put("cougar", R.drawable.cougar);
//		    	put("coyote", R.drawable.coyote);
//		    	put("elk", R.drawable.elk);
//		    	put("elk_bull_s", R.drawable.elk_bull_s);
//		    	put("ephemeroptera", R.drawable.ephemeroptera);
//		    	put("glizzry", R.drawable.glizzry);
//		    	put("grayling", R.drawable.grayling);
//		    	put("great_horned_owl", R.drawable.great_horned_owl);
//		    	put("grus_americana", R.drawable.grus_americana);
//		    	put("hoary_marmot", R.drawable.hoary_marmot);
//		    	put("kabutomushi_mesu", R.drawable.kabutomushi_mesu);
//		    	put("kabutomushi_osu", R.drawable.kabutomushi_osu);
//		    	put("kamakiri", R.drawable.kamakiri);
//		    	put("kawasemi", R.drawable.kawasemi);
//		    	put("kiageha", R.drawable.kiageha);
//		    	put("ladybug", R.drawable.ladybug);
//		    	put("leptinotarsa_decemlineata", R.drawable.leptinotarsa_decemlineata);
//		    	put("lynx", R.drawable.lynx);
//		    	put("machingunlily", R.drawable.machingunlily);
//		    	put("melanoplus_spretus", R.drawable.melanoplus_spretus);
//		    	put("merikabaison", R.drawable.merikabaison);
//		    	put("moose", R.drawable.moose);
//		    	put("motsugo", R.drawable.motsugo);
//		    	put("mountain_tortoise", R.drawable.mountain_tortoise);
//		    	put("nizimasu", R.drawable.nizimasu);
//		    	put("northern_rocky_mountains_gray_wolf", R.drawable.northern_rocky_mountains_gray_wolf);
//		    	put("pinus_banksiana", R.drawable.pinus_banksiana);
//		    	put("rattlesnake", R.drawable.rattlesnake);
//		    	put("rockymountaoinwhitefish", R.drawable.rockymountaoinwhitefish);
//		    	put("sakuramasu", R.drawable.sakuramasu);
//		    	put("spruce_grouse", R.drawable.spruce_grouse);
//		    	put("stellers_jay", R.drawable.stellers_jay);
//		    	put("suzume", R.drawable.suzume);
//		    	put("sympetrum_danae", R.drawable.sympetrum_danae);
//		    	put("virginia_rail", R.drawable.virginia_rail);
//		    	put("western_longnose_sucker", R.drawable.western_longnose_sucker);
//		    	put("yamame", R.drawable.yamame);
		    	// Mori
		    	
		    	// Kasahara
		    	
		    	put("african_jewelfish", R.drawable.african_jewelfish);
		    	put("akanesumire", R.drawable.akanesumire);
		    	put("amerikabaison", R.drawable.amerikabaison);
		    	put("anaguma", R.drawable.anaguma);
		    	put("aosuziageha", R.drawable.aosuziageha);
		    	put("arctic_char", R.drawable.arctic_char);
		    	put("arctic_grayling", R.drawable.arctic_grayling);
		    	put("atlantic_salmon", R.drawable.atlantic_salmon);
		    	put("badger", R.drawable.badger);
		    	put("bald_eagle", R.drawable.bald_eagle);
		    	put("baltimore_oriole", R.drawable.baltimore_oriole);
		    	put("beaver", R.drawable.beaver);
		    	put("big_brown_bat", R.drawable.big_brown_bat);
		    	put("bighorn_sheep", R.drawable.bighorn_sheep);
		    	put("bighornsheep", R.drawable.bighornsheep);
		    	put("bison", R.drawable.bison);
		    	put("black_backed_woodpecker", R.drawable.black_backed_woodpecker);
		    	put("black_bear", R.drawable.black_bear);
		    	put("blackbass", R.drawable.blackbass);
		    	put("boreal_chorus_frog", R.drawable.boreal_chorus_frog);
		    	put("boreal_frog", R.drawable.boreal_frog);
		    	put("brilliant_blue_discus_fish", R.drawable.brilliant_blue_discus_fish);
		    	put("broad_winged_hawk", R.drawable.broad_winged_hawk);
		    	put("brook_stickleback", R.drawable.brook_stickleback);
		    	put("brook_trout", R.drawable.brook_trout);
		    	//put("brown_trout", R.drawable.brown_trout);
		    	put("brown_trout", R.drawable.brown_trout_s);
		    	put("bull_ramparts", R.drawable.bull_ramparts);
		    	put("bull_trout", R.drawable.bull_trout);
		    	put("bushy_tailed_wood_rat", R.drawable.bushy_tailed_wood_rat);
		    	put("canada_lynx", R.drawable.canada_lynx);
		    	put("canadian_toad", R.drawable.canadian_toad);
		    	put("cgs_s", R.drawable.cgs_s);
		    	put("cisco", R.drawable.cisco);
		    	put("columbia_spotted_frog", R.drawable.columbia_spotted_frog);
		    	put("columbian_mantled_ground_squirrel", R.drawable.columbian_mantled_ground_squirrel);
		    	put("convict_cichlid", R.drawable.convict_cichlid);
		    	put("cougar", R.drawable.cougar);
		    	put("coyote", R.drawable.coyote);
		    	put("deer_mouse", R.drawable.deer_mouse);
		    	put("dolden_mantled_ground_squirrel", R.drawable.dolden_mantled_ground_squirrel);
		    	put("elk", R.drawable.elk);
		    	put("ephemeroptera", R.drawable.ephemeroptera);
		    	put("ermine", R.drawable.ermine);
		    	put("fathead_minnow", R.drawable.fathead_minnow);
		    	put("fisher", R.drawable.fisher);
		    	put("freshwater_angelfish", R.drawable.freshwater_angelfish);
		    	put("glacier_lily", R.drawable.glacier_lily);
		    	put("glizzry", R.drawable.glizzry);
		    	put("goldfish", R.drawable.goldfish);
		    	put("gray_wolf1", R.drawable.gray_wolf1);
		    	put("grayling", R.drawable.grayling);
		    	put("great_horned_owl", R.drawable.great_horned_owl);
		    	put("green_swordtail", R.drawable.green_swordtail);
		    	put("grus_americana", R.drawable.grus_americana);
		    	put("guppy", R.drawable.guppy);
		    	put("hoary_marmot", R.drawable.hoary_marmot);
		    	put("indian_paintbrush1", R.drawable.indian_paintbrush1);
		    	put("k28", R.drawable.k28);
		    	put("k29", R.drawable.k29);
		    	put("k30", R.drawable.k30);
		    	put("k31", R.drawable.k31);
		    	put("k32", R.drawable.k32);
		    	put("k33", R.drawable.k33);
		    	put("k34", R.drawable.k34);
		    	put("kabutomushi_mesu", R.drawable.kabutomushi_mesu);
		    	put("kabutomushi_osu", R.drawable.kabutomushi_osu);
		    	put("kamakiri", R.drawable.kamakiri);
		    	put("kawasemi", R.drawable.kawasemi);
		    	put("kiageha", R.drawable.kiageha);
		    	put("lady_slipper", R.drawable.lady_slipper);
		    	put("ladybug", R.drawable.ladybug);
		    	put("lake_club", R.drawable.lake_club);
		    	put("lake_trout1", R.drawable.lake_trout1);
		    	put("lake_whitefish1", R.drawable.lake_whitefish1);
		    	put("least_chipmunk", R.drawable.least_chipmunk);
		    	put("least_weasel", R.drawable.least_weasel);
		    	put("leptinotarsa_decemlineata", R.drawable.leptinotarsa_decemlineata);
		    	put("little_brown_bat", R.drawable.little_brown_bat);
		    	put("long_eared_bat", R.drawable.long_eared_bat);
		    	put("long_legged_bat", R.drawable.long_legged_bat);
		    	put("long_tailed_vole", R.drawable.long_tailed_vole);
		    	put("long_tailed_weasel", R.drawable.long_tailed_weasel);
		    	put("long_toed_salamander", R.drawable.long_toed_salamander);
		    	put("longnose_dace", R.drawable.longnose_dace);
		    	put("longnose_sucker", R.drawable.longnose_sucker);
		    	put("lynx", R.drawable.lynx);
		    	put("m1", R.drawable.m1);
		    	put("m10", R.drawable.m10);
		    	put("m11", R.drawable.m11);
		    	put("m12", R.drawable.m12);
		    	put("m13", R.drawable.m13);
		    	put("m14", R.drawable.m14);
		    	put("m15", R.drawable.m15);
		    	put("m16", R.drawable.m16);
		    	put("m17", R.drawable.m17);
		    	put("m18", R.drawable.m18);
		    	put("m19", R.drawable.m19);
		    	put("m2", R.drawable.m2);
		    	put("m20", R.drawable.m20);
		    	put("m21", R.drawable.m21);
		    	put("m22", R.drawable.m22);
		    	put("m23", R.drawable.m23);
		    	put("m24", R.drawable.m24);
		    	put("m25", R.drawable.m25);
		    	put("m26", R.drawable.m26);
		    	put("m27", R.drawable.m27);
		    	put("m28", R.drawable.m28);
		    	put("m29", R.drawable.m29);
		    	put("m3", R.drawable.m3);
		    	put("m30", R.drawable.m30);
		    	put("m31", R.drawable.m31);
		    	put("m32", R.drawable.m32);
		    	put("m33", R.drawable.m33);
		    	put("m34", R.drawable.m34);
		    	put("m35", R.drawable.m35);
		    	put("m4", R.drawable.m4);
		    	put("m5", R.drawable.m5);
		    	put("m56", R.drawable.m56);
		    	put("m57", R.drawable.m57);
		    	put("m58", R.drawable.m58);
		    	put("m59", R.drawable.m59);
		    	put("m6", R.drawable.m6);
		    	put("m60", R.drawable.m60);
		    	put("m61", R.drawable.m61);
		    	put("m62", R.drawable.m62);
		    	put("m63", R.drawable.m63);
		    	put("m64", R.drawable.m64);
		    	put("m65", R.drawable.m65);
		    	put("m66", R.drawable.m66);
		    	put("m67", R.drawable.m67);
		    	put("m68", R.drawable.m68);
		    	put("m69", R.drawable.m69);
		    	put("m7", R.drawable.m7);
		    	put("m70", R.drawable.m70);
		    	put("m71", R.drawable.m71);
		    	put("m72", R.drawable.m72);
		    	put("m73", R.drawable.m73);
		    	put("m74", R.drawable.m74);
		    	put("m75", R.drawable.m75);
		    	put("m8", R.drawable.m8);
		    	put("m9", R.drawable.m9);
		    	put("machingunlily", R.drawable.machingunlily);
		    	put("machingunlily_s", R.drawable.machingunlily_s);
		    	put("marmot_s", R.drawable.marmot_s);
		    	put("marten", R.drawable.marten);
		    	put("meadow_vole", R.drawable.meadow_vole);
		    	put("melanoplus_spretus", R.drawable.melanoplus_spretus);
		    	put("merikabaison", R.drawable.merikabaison);
		    	put("mink", R.drawable.mink);
		    	put("moose", R.drawable.moose);
		    	put("motsugo", R.drawable.motsugo);
		    	put("mountain_goat", R.drawable.mountain_goat);
		    	put("mountain_sucker", R.drawable.mountain_sucker);
		    	put("mountain_tortoise", R.drawable.mountain_tortoise);
		    	put("muskrat", R.drawable.muskrat);
		    	put("nizimasu", R.drawable.nizimasu);
		    	put("northern_flying_squirrel", R.drawable.northern_flying_squirrel);
		    	put("northern_leopard_frog", R.drawable.northern_leopard_frog);
		    	put("northern_redbelly_dace", R.drawable.northern_redbelly_dace);
		    	put("northern_rocky_mountains_gray_wolf", R.drawable.northern_rocky_mountains_gray_wolf);
		    	put("pinus_banksiana", R.drawable.pinus_banksiana);
		    	put("porcupine", R.drawable.porcupine);
		    	put("prairie_crocus", R.drawable.prairie_crocus);
		    	put("rattlesnake", R.drawable.rattlesnake);
		    	put("red_fox", R.drawable.red_fox);
		    	put("red_squirrel", R.drawable.red_squirrel);
		    	put("river_otter", R.drawable.river_otter);
		    	put("rockymountaoinwhitefish", R.drawable.rockymountaoinwhitefish);
		    	put("rubber_boa", R.drawable.rubber_boa);
		    	put("s40", R.drawable.s40);
		    	put("s41", R.drawable.s41);
		    	put("s42", R.drawable.s42);
		    	put("s43", R.drawable.s43);
		    	put("s44", R.drawable.s44);
		    	put("s45", R.drawable.s45);
		    	put("s46", R.drawable.s46);
		    	put("s47", R.drawable.s47);
		    	put("s48", R.drawable.s48);
		    	put("s49", R.drawable.s49);
		    	put("s50", R.drawable.s50);
		    	put("s51", R.drawable.s51);
		    	put("sailfin_molly", R.drawable.sailfin_molly);
		    	put("sakuramasu", R.drawable.sakuramasu);
		    	put("silver_haired_bat", R.drawable.silver_haired_bat);
		    	put("southern_red_backed_vole", R.drawable.southern_red_backed_vole);
		    	put("spruce_grouse", R.drawable.spruce_grouse);
		    	put("stellers_jay", R.drawable.stellers_jay);
		    	put("striped_skunk", R.drawable.striped_skunk);
		    	put("suzume", R.drawable.suzume);
		    	put("sympetrum_danae", R.drawable.sympetrum_danae);
		    	put("tiger_salamander", R.drawable.tiger_salamander);
		    	put("trout_perch", R.drawable.trout_perch);
		    	put("venus_slipper", R.drawable.venus_slipper);
		    	put("virginia_rail", R.drawable.virginia_rail);
		    	put("wandering_garter_snake", R.drawable.wandering_garter_snake);
		    	put("water_vole", R.drawable.water_vole);
		    	put("western_jumping_mouse", R.drawable.western_jumping_mouse);
		    	put("western_longnose_sucker", R.drawable.western_longnose_sucker);
		    	put("western_mosquitofish", R.drawable.western_mosquitofish);
		    	put("western_toad", R.drawable.western_toad);
		    	put("white_sucker", R.drawable.white_sucker);
		    	put("white_wolf", R.drawable.white_wolf);
		    	put("wolf", R.drawable.wolf);
		    	put("wolverine", R.drawable.wolverine);
		    	put("wood_frog", R.drawable.wood_frog);
		    	put("woodland_caribou", R.drawable.woodland_caribou);
		    	put("yamame", R.drawable.yamame);
		    	put("yellow_pine_chipmunk", R.drawable.yellow_pine_chipmunk);

		}};
	public static Integer[] mThumbIds = {
//		R.drawable.cgs_s,
//		R.drawable.marmot_s,
//		R.drawable.elk_bull_s,
//		R.drawable.machingunlily_s,
//		R.drawable.akanesumire,
//		R.drawable.anaguma,
//		R.drawable.aosuziageha,
//		R.drawable.bald_eagle,
//		R.drawable.bighornsheep,
//		R.drawable.black_backed_woodpecker,
//		R.drawable.blackbass,
//		R.drawable.brilliant_blue_discus_fish,
//		R.drawable.brown_trout,
//		R.drawable.cougar,
//		R.drawable.coyote,
//		R.drawable.elk,
//		R.drawable.ephemeroptera,
//		R.drawable.glizzry,
//		R.drawable.grayling,
//		R.drawable.kabutomushi_mesu,
//		R.drawable.kabutomushi_osu,
//		R.drawable.kamakiri,
//		R.drawable.kawasemi,
//		R.drawable.kiageha,
//		R.drawable.ladybug,
//		R.drawable.leptinotarsa_decemlineata,
//		R.drawable.machingunlily,
//		R.drawable.melanoplus_spretus,
//		R.drawable.motsugo,
//		R.drawable.mountain_tortoise,
//		R.drawable.nizimasu,
//		R.drawable.pinus_banksiana,
//		R.drawable.rattlesnake,
//		R.drawable.rockymountaoinwhitefish,
//		R.drawable.sakuramasu,
//		R.drawable.spruce_grouse,
//		R.drawable.stellers_jay,
//		R.drawable.suzume,
//		R.drawable.sympetrum_danae,
//		R.drawable.western_longnose_sucker,
//		R.drawable.yamame
		R.drawable.african_jewelfish,
		R.drawable.akanesumire,
		R.drawable.amerikabaison,
		R.drawable.anaguma,
		R.drawable.aosuziageha,
		R.drawable.arctic_char,
		R.drawable.arctic_grayling,
		R.drawable.atlantic_salmon,
		R.drawable.badger,
		R.drawable.bald_eagle,
		R.drawable.baltimore_oriole,
		R.drawable.beaver,
		R.drawable.big_brown_bat,
		R.drawable.bighorn_sheep,
		R.drawable.bighornsheep,
		R.drawable.bison,
		R.drawable.black_backed_woodpecker,
		R.drawable.black_bear,
		R.drawable.blackbass,
		R.drawable.boreal_chorus_frog,
		R.drawable.boreal_frog,
		R.drawable.brilliant_blue_discus_fish,
		R.drawable.broad_winged_hawk,
		R.drawable.brook_stickleback,
		R.drawable.brook_trout,
		R.drawable.brown_trout,
		R.drawable.bull_ramparts,
		R.drawable.bull_trout,
		R.drawable.bushy_tailed_wood_rat,
		R.drawable.canada_lynx,
		R.drawable.canadian_toad,
		R.drawable.cgs_s,
		R.drawable.cisco,
		R.drawable.columbia_spotted_frog,
		R.drawable.columbian_mantled_ground_squirrel,
		R.drawable.convict_cichlid,
		R.drawable.cougar,
		R.drawable.coyote,
		R.drawable.deer_mouse,
		R.drawable.dolden_mantled_ground_squirrel,
		R.drawable.elk,
		R.drawable.ephemeroptera,
		R.drawable.ermine,
		R.drawable.fathead_minnow,
		R.drawable.fisher,
		R.drawable.freshwater_angelfish,
		R.drawable.glacier_lily,
		R.drawable.glizzry,
		R.drawable.goldfish,
		R.drawable.gray_wolf1,
		R.drawable.grayling,
		R.drawable.great_horned_owl,
		R.drawable.green_swordtail,
		R.drawable.grus_americana,
		R.drawable.guppy,
		R.drawable.hoary_marmot,
		R.drawable.indian_paintbrush1,
		R.drawable.k28,
		R.drawable.k29,
		R.drawable.k30,
		R.drawable.k31,
		R.drawable.k32,
		R.drawable.k33,
		R.drawable.k34,
		R.drawable.kabutomushi_mesu,
		R.drawable.kabutomushi_osu,
		R.drawable.kamakiri,
		R.drawable.kawasemi,
		R.drawable.kiageha,
		R.drawable.lady_slipper,
		R.drawable.ladybug,
		R.drawable.lake_club,
		R.drawable.lake_trout1,
		R.drawable.lake_whitefish1,
		R.drawable.least_chipmunk,
		R.drawable.least_weasel,
		R.drawable.leptinotarsa_decemlineata,
		R.drawable.little_brown_bat,
		R.drawable.long_eared_bat,
		R.drawable.long_legged_bat,
		R.drawable.long_tailed_vole,
		R.drawable.long_tailed_weasel,
		R.drawable.long_toed_salamander,
		R.drawable.longnose_dace,
		R.drawable.longnose_sucker,
		R.drawable.lynx,
		R.drawable.m1,
		R.drawable.m10,
		R.drawable.m11,
		R.drawable.m12,
		R.drawable.m13,
		R.drawable.m14,
		R.drawable.m15,
		R.drawable.m16,
		R.drawable.m17,
		R.drawable.m18,
		R.drawable.m19,
		R.drawable.m2,
		R.drawable.m20,
		R.drawable.m21,
		R.drawable.m22,
		R.drawable.m23,
		R.drawable.m24,
		R.drawable.m25,
		R.drawable.m26,
		R.drawable.m27,
		R.drawable.m28,
		R.drawable.m29,
		R.drawable.m3,
		R.drawable.m30,
		R.drawable.m31,
		R.drawable.m32,
		R.drawable.m33,
		R.drawable.m34,
		R.drawable.m35,
		R.drawable.m4,
		R.drawable.m5,
		R.drawable.m56,
		R.drawable.m57,
		R.drawable.m58,
		R.drawable.m59,
		R.drawable.m6,
		R.drawable.m60,
		R.drawable.m61,
		R.drawable.m62,
		R.drawable.m63,
		R.drawable.m64,
		R.drawable.m65,
		R.drawable.m66,
		R.drawable.m67,
		R.drawable.m68,
		R.drawable.m69,
		R.drawable.m7,
		R.drawable.m70,
		R.drawable.m71,
		R.drawable.m72,
		R.drawable.m73,
		R.drawable.m74,
		R.drawable.m75,
		R.drawable.m8,
		R.drawable.m9,
		R.drawable.machingunlily,
		R.drawable.machingunlily_s,
		R.drawable.marmot_s,
		R.drawable.marten,
		R.drawable.meadow_vole,
		R.drawable.melanoplus_spretus,
		R.drawable.merikabaison,
		R.drawable.mink,
		R.drawable.moose,
		R.drawable.motsugo,
		R.drawable.mountain_goat,
		R.drawable.mountain_sucker,
		R.drawable.mountain_tortoise,
		R.drawable.muskrat,
		R.drawable.nizimasu,
		R.drawable.northern_flying_squirrel,
		R.drawable.northern_leopard_frog,
		R.drawable.northern_redbelly_dace,
		R.drawable.northern_rocky_mountains_gray_wolf,
		R.drawable.pinus_banksiana,
		R.drawable.porcupine,
		R.drawable.prairie_crocus,
		R.drawable.rattlesnake,
		R.drawable.red_fox,
		R.drawable.red_squirrel,
		R.drawable.river_otter,
		R.drawable.rockymountaoinwhitefish,
		R.drawable.rubber_boa,
		R.drawable.s40,
		R.drawable.s41,
		R.drawable.s42,
		R.drawable.s43,
		R.drawable.s44,
		R.drawable.s45,
		R.drawable.s46,
		R.drawable.s47,
		R.drawable.s48,
		R.drawable.s49,
		R.drawable.s50,
		R.drawable.s51,
		R.drawable.sailfin_molly,
		R.drawable.sakuramasu,
		R.drawable.silver_haired_bat,
		R.drawable.southern_red_backed_vole,
		R.drawable.spruce_grouse,
		R.drawable.stellers_jay,
		R.drawable.striped_skunk,
		R.drawable.suzume,
		R.drawable.sympetrum_danae,
		R.drawable.tiger_salamander,
		R.drawable.trout_perch,
		R.drawable.venus_slipper,
		R.drawable.virginia_rail,
		R.drawable.wandering_garter_snake,
		R.drawable.water_vole,
		R.drawable.western_jumping_mouse,
		R.drawable.western_longnose_sucker,
		R.drawable.western_mosquitofish,
		R.drawable.western_toad,
		R.drawable.white_sucker,
		R.drawable.white_wolf,
		R.drawable.wolf,
		R.drawable.wolverine,
		R.drawable.wood_frog,
		R.drawable.woodland_caribou,
		R.drawable.yamame,
		R.drawable.yellow_pine_chipmunk

		};

	public static String[] mTitles = {
		"Elk",
		"Machingun Lily",
		"Marmot" 
		};

	MyDatabaseHelper helper = null;
	SQLiteDatabase db = null;
	
	PorterStemmer stemmer = null;
	StopwordsHelper stopwordsHelper = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        helper = new MyDatabaseHelper(SearchActivity.this);
		db = helper.getWritableDatabase();
        
		stemmer = new PorterStemmer();
		stopwordsHelper = new StopwordsHelper();
 
		
        ListView listView = (ListView) findViewById(android.R.id.list);

        // リストビューのアイテムがクリックされた時に呼び出されるコールバックリスナーを登録します
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //@Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent intent = new Intent(SearchActivity.this, Detail.class);
                intent.putExtra("postion", position);
                startActivity(intent);
            }
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    Intent intent = null;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.search_to_cg:
//            	// change to 3dcg activity
//                intent = new Intent(SearchActivity.this, NyARToolkitAndroidActivity.class);
//                startActivity(intent);
//                break;
            case R.id.search_to_quest:
            	// change to quest activity
                intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }
    
    public void showResultList(View v) {
    	
    	//get search keyword from textbox
    	EditText editText = (EditText) findViewById(R.id.editText1);
    	SpannableStringBuilder query = (SpannableStringBuilder)editText.getText();
    	System.out.println(query.toString());
   	
    	// query termをDBに格納する
    	LogWriter.sdput(query.toString());
    	/////////

		// Split terms
//		String[] splitedTerms = query.toString().split(" ");
//
//		// 重複なしリストに格納
//		for (String aTerm : splitedTerms) {
//			// remove stop words
//			if (stopwordsHelper.isStopword(aTerm.toLowerCase())) {
//				continue;
//			}
//			
//			// stemming
//			termSet.put(s.stem(aTerm.toLowerCase()), scores[i]);
//		}
//
//	}
			
	// データベース挿入処理
//	Iterator<String> it = termMap.keySet().iterator();
//	ContentValues val = new ContentValues();
//	while (it.hasNext()) {
//		String term = (String) it.next();
//		Float gscore = termMap.get(term);
//
//		Double score = 0.0;

//		// 既に存在していたら、更新
//		//
//		Cursor cursor = null;
//		cursor = db.rawQuery("SELECT score, w_speech, w_speech_nogscore, w_speech_nogscore_nocnum, w_speech_nodb, w_speech_nodb_nogscore, w_speech_nodb_nogscore_nocnum, w_speech_freq FROM "
//				+ DatabaseHelper.TABLE_PREFERENCE_THESAURUS
//				+ " WHERE term = ? LIMIT 1", new String[] { term });
//
//		if (cursor.getCount() > 0) {
//			cursor.moveToFirst();
//			score = cursor.getDouble(0);

//			
//			Double weight = w_speech + (1.0 + w_speech) * gscore;

//			
//			val.put("w_speech", weight);

//			
//			db.update(DatabaseHelper.TABLE_PREFERENCE_THESAURUS,
//					val, "term = ?", new String[] { term });
//			//out += "update :" + term + " (" + weight + ")\n";
//			System.out.println("update :" + term + " (" + weight
//					+ ")");
//		} else {
//
//			Double weight = 0.0 + gscore;
//			Double weight_nogscore = 0.0 + 1.0/validCnt;
//			Double weight_nogscore_nocnum = 1.0;
//			Double weight_nodb = 0.0 + gscore;
//			Double weight_nodb_nogscore = 0.0 + 1.0/validCnt;
//			Double weight_nodb_nogscore_nocnum = 0.0 + 1.0;
//			Double weight_freq = 0.0 + 1.0;
//			
//			val.put("term", term);
//			val.put("w_speech", weight);

//		}
//
//	}
//
//	//resultsString = "Success";
//} catch (Exception e) {
//	//resultsString = "Error";
//	Log.e("ERROR", e.toString());
//} finally {
//	db.setTransactionSuccessful();
//	db.endTransaction();
//}
    	
    	////////
    	
// ネットが使えないときはコメント    	
    	SearchDataSource search = new SearchDataSource(this.getResources());
    	search(search, query.toString());
		List<BindData> objects = new ArrayList<BindData>();
		for(int i = 0; i < documents.size(); i++) {
			// debug
			
			try {
				BindData data =
					//new BindData(documents.get(i).getName(), mThumbIds[i]);
						new BindData(documents.get(i).getName(), thumbIdMap.get(documents.get(i).getImage()));
				
				
				objects.add(data);
			} catch (NullPointerException e) {
				System.out.println(documents.get(i).getName());
			}
		}
		setListAdapter(new ImageAdapter(this, objects));

    }
    
    private static boolean search(SearchDataSource source, String query) {
		if (source==null) return false;
		
		String url = null;
		try {
			url = source.createRequestURL(0, 0, 0, 0, query);
			Log.v("SEARCH", "url="+url);
		} catch (NullPointerException e) {
			return false;
		}
    	
		
		try {
			documents = source.parseSearchResult(url);
		} catch (NullPointerException e) {
			return false;
		}

    	//ARData.addMarkers(markers);
    	return true;
    }
}
