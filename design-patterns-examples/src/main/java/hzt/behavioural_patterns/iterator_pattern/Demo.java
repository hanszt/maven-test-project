package hzt.behavioural_patterns.iterator_pattern;

import hzt.behavioural_patterns.iterator_pattern.profile.Profile;
import hzt.behavioural_patterns.iterator_pattern.social_networks.Facebook;
import hzt.behavioural_patterns.iterator_pattern.social_networks.LinkedIn;
import hzt.behavioural_patterns.iterator_pattern.social_networks.SocialNetwork;
import hzt.behavioural_patterns.iterator_pattern.spammer.SocialSpammer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Demo class. Everything comes together here.
 */
public class Demo {

    private static final Scanner INPUT = new Scanner(System.in);
    private static final String DEMO_MAIL_ADDRESS = "anna.smith@bing.com";

    private static final String LINKEDIN_ID = "2";

    public static void main(String... args) {
        System.out.println("Please specify social network to target spam tool (default:Facebook):");
        System.out.println("1. Facebook");
        System.out.println("2. LinkedIn");
        String choice = INPUT.nextLine();

        SocialNetwork network = choice.equals(LINKEDIN_ID) ? new LinkedIn(createTestProfiles()) : new Facebook(createTestProfiles());
        String message = choice.equals(LINKEDIN_ID) ?
                "Hey! This is Anna's boss Jason. Anna told me you would be interested in [link]." :
                "Hey! This is Anna's friend Josh. Can you do me a favor and like this post [link]?";
        SocialSpammer spammer = new SocialSpammer(network);
        spammer.sendSpamToContacts(DEMO_MAIL_ADDRESS, message);
    }

    public static List<Profile> createTestProfiles() {
        List<Profile> data = new ArrayList<>();
        data.add(new Profile(DEMO_MAIL_ADDRESS, "Anna Smith", "friends:mad_max@ya.com", "friends:catwoman@yahoo.com", "coworkers:sam@amazon.com"));
        data.add(new Profile("mad_max@ya.com", "Maximilian", "friends:anna.smith@bing.com", "coworkers:sam@amazon.com"));
        data.add(new Profile("bill@microsoft.eu", "Billie", "coworkers:avanger@ukr.net"));
        data.add(new Profile("avanger@ukr.net", "John Day", "coworkers:bill@microsoft.eu"));
        data.add(new Profile("sam@amazon.com", "Sam Kitting", "coworkers:anna.smith@bing.com", "coworkers:mad_max@ya.com", "friends:catwoman@yahoo.com"));
        data.add(new Profile("catwoman@yahoo.com", "Liza", "friends:anna.smith@bing.com", "friends:sam@amazon.com"));
        return data;
    }
}
