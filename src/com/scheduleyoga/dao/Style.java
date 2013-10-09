package com.scheduleyoga.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import com.scheduleyoga.parser.Helper;

public class Style {
	static public final String NAME_BEGINNER = "Beginner"; 
	static public final String NAME_INTERMEDIATE = "Intermediate";
	static public final String NAME_ADVANCED = "Advanced";	
	
	static public final String NAME_HOT_YOGA = "Hot";
	static public final String NAME_VINYASA = "Vinyasa";	
	static public final String NAME_ISHTA = "Ishta";
	static public final String NAME_IYENGAR = "Iyengar";
	static public final String NAME_ASHTANGA = "Ashtanga";
	static public final String NAME_BIKRAM = "Bikram";
	static public final String NAME_POWER = "Power";
	static public final String NAME_KUNDALINI = "Kundalini";
	static public final String NAME_TUNE_UP = "Tune Up";
	static public final String NAME_YIN = "Yin";
	static public final String NAME_MEDITATION = "Meditation";
	static public final String NAME_KRIPALU = "Kripalu";
	static public final String NAME_NIA = "Nia";
	static public final String NAME_PRANAYAMA = "Pranayama";
	static public final String NAME_FORREST = "Forrest";
	static public final String NAME_ANUSARA = "Anusara";
	
	static public final String NAME_PRENATAL = "Prenatal";
	static public final String NAME_BABY = "Baby";
	static public final String NAME_KIDS = "Kids";
	static public final String NAME_RESTORATIVE = "Restorative";
	
	static public final String NAME_NON_YOGA = "Non-Yoga";
	
	private String name;
	
	private String[] allNames = {NAME_BEGINNER,NAME_INTERMEDIATE,NAME_ADVANCED,NAME_HOT_YOGA};
	private Set<String> styleNames = new TreeSet<String>(Arrays.asList(allNames));
	
	
	public static Style createNew(String styleName){
		return new Style(styleName);
	}
	
	public static Style createNewFromUrl(String styleNameForURL){
		 
		Set<String> styleNames = Style.allStyleNames();
		
		Transformer transf = new Transformer() {
            public Object transform(Object str) {
                return ( (String)str).toLowerCase().replaceAll("\\W+", " ").replaceAll("\\s+", "-");
            }
        };		
		CollectionUtils.transform(styleNames,transf); //Collection<String> lowerCaseStrings = 
		
		if (styleNames.contains(styleNameForURL)){
			String readableNam = WordUtils.capitalizeFully(styleNameForURL.replaceAll("-", " "));
			return new Style(readableNam);
		} else {
			return null;
		}
	}
	
	protected Style(String styleName){
		name = styleName;
	}
	
	public static List<Style> allStyles(){
		Set<String> styleNames = allStyleNames();
			
		List<Style> styles = new ArrayList<Style>();
		for(String styleName : styleNames){
			styles.add(Style.createNew(styleName));
		}
		
		return styles;
	}

	/**
	 * @return
	 */
	protected static Set<String> allStyleNames() {
		Set<String> styleNames = new TreeSet<String>();
		styleNames.add(NAME_BEGINNER);
		styleNames.add(NAME_INTERMEDIATE);
		styleNames.add(NAME_ADVANCED);
		styleNames.add(NAME_HOT_YOGA);
		styleNames.add(NAME_VINYASA);
		styleNames.add(NAME_ISHTA);
		styleNames.add(NAME_IYENGAR);
		styleNames.add(NAME_ASHTANGA);
		styleNames.add(NAME_BIKRAM);
		styleNames.add(NAME_POWER);
		styleNames.add(NAME_KUNDALINI);
		styleNames.add(NAME_TUNE_UP);
		styleNames.add(NAME_YIN);
		styleNames.add(NAME_MEDITATION);
		styleNames.add(NAME_KRIPALU);
		styleNames.add(NAME_NIA);
		styleNames.add(NAME_PRANAYAMA);
		styleNames.add(NAME_FORREST);
		styleNames.add(NAME_ANUSARA);
		styleNames.add(NAME_PRENATAL);
		styleNames.add(NAME_BABY);
		styleNames.add(NAME_KIDS);
		styleNames.add(NAME_RESTORATIVE);
		return styleNames;
	}
	
	public static Set<String> styleNamesFromClassName(String className){
		
		className = className.toLowerCase();
		className = className.replaceAll("\\W+", " "); //replace any non-word characters with space
		className = className.trim();
		className = className.replaceAll("\\s+", " "); //replace any sequence of white space characters with a single space
		
		Set<String> styleNames = new HashSet<String>();
		
		Map<String, String> containsMap = new HashMap<String, String>();
		Map<String, String> startsWithMap = new HashMap<String, String>();
		
		containsMap.put("basic", Style.NAME_BEGINNER);
		containsMap.put("beginner", Style.NAME_BEGINNER);
		containsMap.put("beginning", Style.NAME_BEGINNER);
		containsMap.put(" beg ", Style.NAME_BEGINNER);
		startsWithMap.put("beg ", Style.NAME_BEGINNER);
		containsMap.put("level i ", Style.NAME_BEGINNER);
		containsMap.put("level 1", Style.NAME_BEGINNER);
		containsMap.put("intro", Style.NAME_BEGINNER);
		containsMap.put("easy", Style.NAME_BEGINNER);
		containsMap.put("gentle", Style.NAME_BEGINNER);

		containsMap.put("intermediate", Style.NAME_INTERMEDIATE);
		containsMap.put(" int ", Style.NAME_INTERMEDIATE);
		startsWithMap.put("int", Style.NAME_INTERMEDIATE);
		containsMap.put("level ii ", Style.NAME_INTERMEDIATE);
		containsMap.put("level i ii ", Style.NAME_INTERMEDIATE);
		containsMap.put("level 2 ", Style.NAME_INTERMEDIATE);
		containsMap.put("level 1 2 ", Style.NAME_INTERMEDIATE);
		containsMap.put("levels 2 ", Style.NAME_INTERMEDIATE);
		
		containsMap.put("advanced", Style.NAME_ADVANCED);
		containsMap.put(" adv ", Style.NAME_ADVANCED);
		containsMap.put("level ii iii ", Style.NAME_ADVANCED);
		containsMap.put("level 2 3 ", Style.NAME_ADVANCED);
		containsMap.put("levels 2 3", Style.NAME_ADVANCED);
		
		containsMap.put("hot ", Style.NAME_HOT_YOGA);
		containsMap.put("heated", Style.NAME_HOT_YOGA);
		containsMap.put("sweat", Style.NAME_HOT_YOGA);
		
		containsMap.put("vinyasa", Style.NAME_VINYASA);
		containsMap.put("ishta", Style.NAME_ISHTA);
		containsMap.put("iyengar", Style.NAME_IYENGAR);
		containsMap.put("ashtanga", Style.NAME_ASHTANGA);
		containsMap.put("mysore", Style.NAME_ASHTANGA);
		containsMap.put("bikram", Style.NAME_BIKRAM);
		containsMap.put("power", Style.NAME_POWER);
		containsMap.put("kundalini", Style.NAME_KUNDALINI);
		containsMap.put("tune up", Style.NAME_TUNE_UP);
		containsMap.put("yin", Style.NAME_YIN);
		containsMap.put("medit", Style.NAME_MEDITATION);
		containsMap.put("kripalu", Style.NAME_KRIPALU);
		containsMap.put("nia", Style.NAME_NIA);
		containsMap.put("pranayama", Style.NAME_PRANAYAMA);
		containsMap.put("forrest", Style.NAME_FORREST);
		containsMap.put("anusara", Style.NAME_ANUSARA);
		
		containsMap.put("prenatal", Style.NAME_PRENATAL);
		containsMap.put("pre natal", Style.NAME_PRENATAL);
		
		containsMap.put("mommy", Style.NAME_BABY);
		containsMap.put("infant", Style.NAME_BABY);
		containsMap.put("toddler", Style.NAME_BABY);
		containsMap.put("crawl", Style.NAME_BABY);
		containsMap.put("postnatal", Style.NAME_BABY);
		containsMap.put("post natal", Style.NAME_BABY);
		containsMap.put("baby", Style.NAME_BABY);
		
		containsMap.put("kids", Style.NAME_KIDS);
		containsMap.put("parent", Style.NAME_KIDS);
		containsMap.put("child", Style.NAME_KIDS);
		containsMap.put("tween", Style.NAME_KIDS);
		containsMap.put("teen", Style.NAME_KIDS);
		containsMap.put("year old", Style.NAME_KIDS);
		containsMap.put("family", Style.NAME_KIDS);
		
		containsMap.put("restorative", Style.NAME_RESTORATIVE);
		containsMap.put("restore", Style.NAME_RESTORATIVE);
		containsMap.put("revival", Style.NAME_RESTORATIVE);
		containsMap.put("relax", Style.NAME_RESTORATIVE);
		
		containsMap.put("danc", Style.NAME_NON_YOGA);
		containsMap.put("pilates", Style.NAME_NON_YOGA);
		containsMap.put("tai chi", Style.NAME_NON_YOGA);
		containsMap.put("zumba", Style.NAME_NON_YOGA);
		containsMap.put("kickboxing", Style.NAME_NON_YOGA);
		
		
		//Loop over containsMap and check every entry
		for (Map.Entry<String, String> entry : containsMap.entrySet()) {
			if(className.contains(entry.getKey())){
				styleNames.add(entry.getValue());
			}
		}
		
		//Loop over startsWithMap and check every entry
		for (Map.Entry<String, String> entry : startsWithMap.entrySet()) {
			if(className.startsWith(entry.getKey())){
				styleNames.add(entry.getValue());
			}
		}
		
		if (className.contains("all levels")){
			styleNames.add(Style.NAME_BEGINNER);
			styleNames.add(Style.NAME_INTERMEDIATE);
			styleNames.add(Style.NAME_ADVANCED);
		}
		
		if (className.contains("mixed level")){
			styleNames.add(Style.NAME_BEGINNER);
			styleNames.add(Style.NAME_INTERMEDIATE);
			styleNames.add(Style.NAME_ADVANCED);
		}
		
		if (className.contains("non heated")){
			styleNames.remove(Style.NAME_HOT_YOGA);
		}
		
		if(styleNames.contains(Style.NAME_NON_YOGA)){
			styleNames.clear(); //Delete any yoga-related tags. and leave only "non-yoga" tag
			styleNames.add(Style.NAME_NON_YOGA);
		}
		
		
		return styleNames;
		
//		List<Style> returnList = new ArrayList<Style>(styleNames.size());
//		for(final String styleName : styleNames){
//			returnList.add(new Style(styleName));
//		}
//		return returnList;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	public String getNameForUrl(){
		return Helper.createNew().nameToURLName(getName());
	}
	
	
}
