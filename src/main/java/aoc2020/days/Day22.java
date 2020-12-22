package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day22
{
    private final List<Player> players = new ArrayList<>();
    private final int totalAmountOfCards;

    public Day22() throws URISyntaxException, IOException
    {
        Path path = Paths.get(getClass().getClassLoader().getResource("day22.txt").toURI());
        var lines = Files.lines(path);
        List<String> input = lines.collect(Collectors.toList());
        lines.close();
        input.add("");

        Player current = null;
        for (var l : input) {
            if (l.startsWith("Player ")) {
                current = new Player(Integer.valueOf(l.substring(7, 8)), new ArrayList<>());
            } else if (l.isBlank()) {
                players.add(current);
                current = null;
            } else {
                current.getCards().add(Integer.valueOf(l));
            }
        }

        totalAmountOfCards = players.stream().map(p -> p.getCards().size())
                .collect(Collectors.summingInt(Integer::valueOf));
    }

    public long part1()
    {
        List<Player> thePlayers = new ArrayList<>();
        for (var p : players) {
            thePlayers.add(new Player(p));
        }

        Player winner = null;
        while (winner == null) {
            playRound(thePlayers);
            winner = doWeHaveAWinner(thePlayers, totalAmountOfCards);
        }

        return getScore(winner.getCards());
    }

    public long part2()
    {
        List<Player> thePlayers = new ArrayList<>();
        for (var p : players) {
            thePlayers.add(new Player(p));
        }

        Set<List<Integer>> p1Appeared = new HashSet<>();
        Set<List<Integer>> p2Appeared = new HashSet<>();

        int winner = -1;
        while (winner == -1) {
            winner = playRoundGame2(thePlayers, totalAmountOfCards, p1Appeared, p2Appeared);
        }

        return getScore(thePlayers.get(winner).getCards());
    }

    private long getScore(List<Integer> cards)
    {
        long score = 0L;
        for (int i = 0; i < cards.size(); i++) {
            score += (cards.get(i) * (cards.size() - i));
        }

        return score;
    }

    private static Player doWeHaveAWinner(List<Player> thePlayers, int totalAmountOfCards)
    {
        for (var p : thePlayers) {
            if (p.getCards().size() == totalAmountOfCards) {
                return p;
            }
        }

        return null;
    }

    private static void playRound(List<Player> thePlayers)
    {
        Player p1 = thePlayers.get(0);
        Player p2 = thePlayers.get(1);

        if (p1.getCards().get(0).intValue() > p2.getCards().get(0).intValue()) {
            Integer p1TopCard = p1.getCards().remove(0);
            Integer p2TopCard = p2.getCards().remove(0);

            p1.getCards().add(p1TopCard);
            p1.getCards().add(p2TopCard);
        } else {
            Integer p1TopCard = p1.getCards().remove(0);
            Integer p2TopCard = p2.getCards().remove(0);

            p2.getCards().add(p2TopCard);
            p2.getCards().add(p1TopCard);
        }
    }

    private static boolean containsDeck(Set<List<Integer>> haystack, List<Integer> needle)
    {
        return haystack.contains(needle);
    }

    private static int playRoundGame2(List<Player> thePlayers, int totalAmountOfCards, Set<List<Integer>> p1Appeared,
            Set<List<Integer>> p2Appeared)
    {
        Player p1 = thePlayers.get(0);
        Player p2 = thePlayers.get(1);

        if (containsDeck(p1Appeared, p1.getCards()) && containsDeck(p2Appeared, p2.getCards())) {
            return 0;
        }

        p1Appeared.add(new ArrayList<>(p1.getCards()));
        p2Appeared.add(new ArrayList<>(p2.getCards()));

        Integer p1TopCard = p1.getCards().remove(0);
        Integer p2TopCard = p2.getCards().remove(0);

        int finalWinner = -1;
        if (p1TopCard <= p1.getCards().size() && p2TopCard <= p2.getCards().size()) {
            Player p1Sub = new Player(p1);
            List<Integer> p1SubList = new ArrayList<>(p1Sub.getCards().subList(0, p1TopCard));
            p1Sub.getCards().clear();
            p1Sub.getCards().addAll(p1SubList);

            Player p2Sub = new Player(p2);
            List<Integer> p2SubList = new ArrayList<>(p2Sub.getCards().subList(0, p2TopCard));
            p2Sub.getCards().clear();
            p2Sub.getCards().addAll(p2SubList);

            Set<List<Integer>> p1AppearedSub = new HashSet<>();
            Set<List<Integer>> p2AppearedSub = new HashSet<>();
            while (finalWinner == -1) {
                finalWinner = playRoundGame2(List.of(p1Sub, p2Sub), totalAmountOfCards, p1AppearedSub, p2AppearedSub);
            }
        } else {
            if (p1TopCard.intValue() > p2TopCard.intValue()) {
                finalWinner = 0;
            } else {
                finalWinner = 1;
            }
        }

        if (finalWinner == 0) {
            p1.getCards().add(p1TopCard);
            p1.getCards().add(p2TopCard);
        } else if (finalWinner == 1) {
            p2.getCards().add(p2TopCard);
            p2.getCards().add(p1TopCard);
        }

        if (p1.getCards().size() == totalAmountOfCards) {
            return 0;
        }
        if (p2.getCards().size() == totalAmountOfCards) {
            return 1;
        }

        if (p1.getCards().isEmpty() && !p2.getCards().isEmpty()) {
            return 1;
        } else if (p2.getCards().isEmpty() && !p1.getCards().isEmpty()) {
            return 0;
        }

        return -1;
    }

    private static class Player
    {
        private final int playerId;
        private final List<Integer> cards;

        public Player(int playerId, List<Integer> cards)
        {
            super();
            this.playerId = playerId;
            this.cards = cards;
        }

        public Player(Player other)
        {
            super();
            this.playerId = other.getPlayerId();
            this.cards = new ArrayList<>(other.getCards());
        }

        public int getPlayerId()
        {
            return playerId;
        }

        public List<Integer> getCards()
        {
            return cards;
        }

        @Override
        public String toString()
        {
            return "Player [playerId=" + playerId + ", cards=" + cards + "]";
        }
    }
}
