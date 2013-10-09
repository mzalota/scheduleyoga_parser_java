package com.scheduleyoga.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.scheduleyoga.dao.Style;

public class StyleTest {

	@Test
	public void testCreateNew_emptyClassName() {
		
		//EXECUTE
		Set<String> styles = Style.styleNamesFromClassName("");
		
		//ASSERT
		assertEquals(0,styles.size());
	}	
	
	@Test
	public void testCreateNew_Beginners() {
		
		//SET UP
		//List<Style> styles;
		Set<String> styles;
		String YOGA_CLASS_NAME;
		
		//EXECUTE and ASSERT
		YOGA_CLASS_NAME = "Basic/Open Level (1 hour & 15 minutes)"; 
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "basics / intermediate\r\n";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "All Levels Class (Cambridge)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(3,styles.size());
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		assertThat(styles, hasItem(Style.NAME_ADVANCED));
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));
		
		YOGA_CLASS_NAME = "Mixed Level (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(3,styles.size());
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		assertThat(styles, hasItem(Style.NAME_ADVANCED));
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));
		

		
		YOGA_CLASS_NAME = "Vinyasa Basics (Greenwich West)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		assertThat(styles, hasItem(Style.NAME_VINYASA));
	
		YOGA_CLASS_NAME = "Beginner - Atmananda Sequence Level 2/3/4 (1 hour & 25 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "Bikram Beginning Yoga Class - ($10 cash at Studio or $20 CC) (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "Ashtanga Vinyasa- Athletic Beg/Intermediate (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "Beg/Int (ISHTA Downtown)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));

		YOGA_CLASS_NAME = "Yoga Mat-Beginners (Yoga Room - Astoria)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "Level I Yoga (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "Level I/II (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "Yoga Level 1/2 (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "Nia intro/gentle w/Relaxation (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "Introductory Series (1 hour & 30 minutes))";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "Easy Flow (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
		YOGA_CLASS_NAME = "Gentle Hatha (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BEGINNER));
		
	}
	
	@Test
	public void testCreateNew_Intermediate() {
		
		//SET UP
		//List<Style> styles;
		Set<String> styles;
		String YOGA_CLASS_NAME;
		
		//EXECUTE and ASSERT
		YOGA_CLASS_NAME = "Ashtanga Vinyasa- Athletic Beg/Intermediate (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));
		
		YOGA_CLASS_NAME = "Beg/Int (ISHTA Downtown)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));
		
		YOGA_CLASS_NAME = "Level II Yoga (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));
		assertThat(styles, not(hasItem(Style.NAME_BEGINNER)));
		
		YOGA_CLASS_NAME = "Level I/II (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));
		
		YOGA_CLASS_NAME = "Level II/III (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));

		YOGA_CLASS_NAME = "Yoga Level 1/2 (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));
		
		YOGA_CLASS_NAME = "Power Yoga - Level 2/3 (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));
		
		YOGA_CLASS_NAME = " LEVELS 2/3 ";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));
		
		YOGA_CLASS_NAME = "Lunchbox Yoga - Level 2 (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));
		
		YOGA_CLASS_NAME = "Power Yoga - Level 2/3 (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_INTERMEDIATE));
		
	}
	
	@Test
	public void testCreateNew_Advanced() {
		
		//SET UP
		//List<Style> styles;
		Set<String> styles;
		String YOGA_CLASS_NAME;
		
		//EXECUTE and ASSERT
		YOGA_CLASS_NAME = "Intermediate/Advanced Level Vinyasa (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_ADVANCED));
		
		YOGA_CLASS_NAME = "Level II/III (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_ADVANCED));
		
		YOGA_CLASS_NAME = "Power Yoga - Level 2/3 (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_ADVANCED));
		
		YOGA_CLASS_NAME = " LEVELS 2/3 ";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_ADVANCED));
		
		YOGA_CLASS_NAME = "Explore (Int/Adv) - Vinyasa (Hosh Two)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_ADVANCED));
	
	}
	
	@Test
	public void testCreateNew_Hot() {
		
		//SET UP
		//List<Style> styles;
		Set<String> styles;
		String YOGA_CLASS_NAME;
		
		//EXECUTE and ASSERT
		YOGA_CLASS_NAME = "Traditional Hot Yoga";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_HOT_YOGA));
		
		YOGA_CLASS_NAME = "Candlelight Flow (Heated) (Pure West)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_HOT_YOGA));
		
		YOGA_CLASS_NAME = "vinyasa non heated (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, not(hasItem(Style.NAME_HOT_YOGA)));
		
		YOGA_CLASS_NAME = "Hatha Flow Non-Heated (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, not(hasItem(Style.NAME_HOT_YOGA)));
		
		YOGA_CLASS_NAME = "Sweat & Surrender (All levels) (61st Street)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_HOT_YOGA));
		
	}
	
	@Test
	public void testCreateNew_MainStyles() {
		
		//SET UP
		//List<Style> styles;
		Set<String> styles;
		String YOGA_CLASS_NAME;
		
		//EXECUTE and ASSERT
		YOGA_CLASS_NAME = "Ishta (1/2) (Eastside)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_ISHTA));
		
		YOGA_CLASS_NAME = "Iyengar Inspired (Greenwich West)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_IYENGAR));
		
		YOGA_CLASS_NAME = "Ashtanga (2/3) (Westside)"; 
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_ASHTANGA));
		
		YOGA_CLASS_NAME = "Mysore:  Led Primary Series (Pure West)"; 
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_ASHTANGA));
		
		YOGA_CLASS_NAME = "Bikram Yoga - Half Moon Class (Flatiron)"; 
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BIKRAM));
		
		YOGA_CLASS_NAME = "Power Yoga (2/3) ? (Soho)"; 
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_POWER));
		
		YOGA_CLASS_NAME = "Yoga Tune Up®: Restore (Pure West)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_TUNE_UP));
		
		YOGA_CLASS_NAME = "Yin/Yang (OPEN) (Pure West)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_YIN));
		
		YOGA_CLASS_NAME = "Morning Movement & Meditation (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_MEDITATION));
		
		YOGA_CLASS_NAME = "Mod. Kripalu Yoga, Medit. & Deep Relaxation (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_MEDITATION));
		
		YOGA_CLASS_NAME = "Gentle Kripalu / \"Yoga 4 a Better Back\" & Relax (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_KRIPALU));
		
		YOGA_CLASS_NAME = "Nia Movement (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_NIA));
		
		YOGA_CLASS_NAME = "Pranayama, Bandhas, and Mudras (Pure West)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_PRANAYAMA));
		
		YOGA_CLASS_NAME = "Forrest Yoga (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_FORREST));
		
		YOGA_CLASS_NAME = "Anusara-Inspired (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_ANUSARA));
		
		YOGA_CLASS_NAME = "Prenatal - Pre-registration required* (Eastside)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_PRENATAL));
		
		YOGA_CLASS_NAME = "Pre-natal (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_PRENATAL));
		
		YOGA_CLASS_NAME = "Kundalini Yoga (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_KUNDALINI));
		
		YOGA_CLASS_NAME = "Mommy and Me Yoga (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BABY));
		
		YOGA_CLASS_NAME = "MommyShape® Postnatal Ab Repair (Upper Room)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BABY));
		
		YOGA_CLASS_NAME = "Mmm and Me (Infants) (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BABY));
		
		YOGA_CLASS_NAME = "Toddler & Me Yoga (18 mo & up) (Westport)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BABY));
		
		YOGA_CLASS_NAME = "Bby & Me (crawling and up) (Westport)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BABY));
		
		YOGA_CLASS_NAME = "Postnatal Yoga (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BABY));
		
		YOGA_CLASS_NAME = "Post-Natal";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BABY));
		
		YOGA_CLASS_NAME = "Baby and Me at Harlem Yoga Studio (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_BABY));
		
		YOGA_CLASS_NAME = "Parent & Child (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_KIDS));

		YOGA_CLASS_NAME = "Children Yoga (age 3-13) (1 hour & 30 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_KIDS));
		
		YOGA_CLASS_NAME = "Tween  Community Yoga PEACE IN (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_KIDS));
		
		YOGA_CLASS_NAME = "Preteen Yoga Flow at PAZ (2 hours)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_KIDS));
		
		YOGA_CLASS_NAME = "7 -9 year olds (45 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_KIDS));
		
		YOGA_CLASS_NAME = "Family Nia (45 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_KIDS));
			
		YOGA_CLASS_NAME = "Kids Yoga (99 University)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_KIDS));
		
		YOGA_CLASS_NAME = "Restorative (Int/Open) (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_RESTORATIVE));
		
		YOGA_CLASS_NAME = "Flow and Restore (ISHTA Downtown)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_RESTORATIVE));
		
		YOGA_CLASS_NAME = "Revival (1 hour & 35 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_RESTORATIVE));
		
		YOGA_CLASS_NAME = "Noon Yoga  Gentle stretch & relax (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_RESTORATIVE));
		
		YOGA_CLASS_NAME = "5 stages/stretch/relax (1 hour & 20 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertThat(styles, hasItem(Style.NAME_RESTORATIVE));
	}
	
	@Test
	public void testCreateNew_NonYoga() {
		
		//SET UP
		//List<Style> styles;
		Set<String> styles;
		String YOGA_CLASS_NAME;
		
		//EXECUTE and ASSERT
		YOGA_CLASS_NAME = "Belly Dance Basics (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(1,styles.size());
		assertThat(styles, hasItem(Style.NAME_NON_YOGA));
		
		YOGA_CLASS_NAME = "Belly Dancing (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(1,styles.size());
		assertThat(styles, hasItem(Style.NAME_NON_YOGA));
		
		YOGA_CLASS_NAME = "Bellydancing (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(1,styles.size());
		assertThat(styles, hasItem(Style.NAME_NON_YOGA));
		
		YOGA_CLASS_NAME = "African Dance (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(1,styles.size());
		assertThat(styles, hasItem(Style.NAME_NON_YOGA));
		
		YOGA_CLASS_NAME = "Pilates (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(1,styles.size());
		assertThat(styles, hasItem(Style.NAME_NON_YOGA));
		
		YOGA_CLASS_NAME = "Happy Hour Pilates - Level 1/2 (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(1,styles.size());
		assertThat(styles, hasItem(Style.NAME_NON_YOGA));
		
		YOGA_CLASS_NAME = "Gentle Pilates (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(1,styles.size());
		assertThat(styles, hasItem(Style.NAME_NON_YOGA));
		
		YOGA_CLASS_NAME = "Tai Chi Ruler & Tai Chi Chuan (1 hour & 15 minutes)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(1,styles.size());
		assertThat(styles, hasItem(Style.NAME_NON_YOGA));
		
		YOGA_CLASS_NAME = "Brazilian Zumba (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(1,styles.size());
		assertThat(styles, hasItem(Style.NAME_NON_YOGA));
		
		YOGA_CLASS_NAME = "KickBoxing (1 hour)";
		styles = Style.styleNamesFromClassName(YOGA_CLASS_NAME);
		assertEquals(1,styles.size());
		assertThat(styles, hasItem(Style.NAME_NON_YOGA));

		
	}
}
