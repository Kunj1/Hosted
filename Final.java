import java.util.*;

class Final1_1
{
    static Map<String,List<String>> grammar=new HashMap<>();
    static Map<String,Set<String>> first=new HashMap<>();
    static Map<String,Set<String>> follow=new HashMap<>();
    static String start;

    public static void main(String args[])
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter start symbol");
        start=sc.nextLine();

        System.out.println("Enter grammer rules, finish using end");

        while(true){
            String stmt=sc.nextLine();
            if(stmt.equals("end")) break;

            String[] parts=stmt.split("->");
            if(parts.length<2) continue;

            grammar.put(parts[0],new ArrayList<>());
            String[] rhs=parts[1].split("\\|");

            for(String i:rhs) grammar.get(parts[0]).add(i);
        }

        for(String a:grammar.keySet()) calcFirst(a);
        for(String a:grammar.keySet()) calcFollow(a);

        System.out.println("First sets->");
        for(String s:grammar.keySet())
        {
            System.out.println(s+"->"+first.get(s));
        }

        System.out.println("Follow sets->");
        for(String s:grammar.keySet())
        {
            System.out.println(s+"->"+follow.get(s));
        }
    }

    static Set<String> calcFirst(String symbol)
    {
        if(!grammar.containsKey(symbol)) 
        return new HashSet<>(Collections.singletonList(symbol));

        if(first.containsKey(symbol)) return first.get(symbol);

        first.put(symbol,new HashSet<>());
        Set<String> result=new HashSet<>();

        List<String> prod=grammar.get(symbol);
        for(String p:prod)
        {
            for(int i=0;i<p.length();i++)
            {
                String curr=String.valueOf(p.charAt(i));

                if(curr.equals("e"))
                {
                    result.add("e");
                    break;
                }

                Set<String> firstCurr=calcFirst(curr);
                result.addAll(firstCurr);

                if(!firstCurr.contains("e")) break;
            }
        }
        first.put(symbol,result);
        return result;
    }

    static Set<String> calcFollow(String symbol)
    {
        if(follow.containsKey(symbol)) return follow.get((symbol));

        Set<String> result=new HashSet<>();

        if(symbol.equals(start)) result.add("$");

        for(String s:grammar.keySet())
        {
            for(String prod:grammar.get(s))
            {
                for(int i=0;i<prod.length();i++)
                {
                    String curr=String.valueOf(prod.charAt(i));

                    if(curr.equals(symbol))
                    {
                        if(i+1<prod.length())
                        {
                            String next=String.valueOf(prod.charAt(i+1));
                            Set<String> firstNext=calcFirst(next);

                            result.addAll(firstNext);
                            result.remove("e");

                            if(firstNext.contains("e"))
                            result.addAll(calcFollow(s));
                        }
                        else if(!s.equals(symbol))
                        result.addAll(calcFollow(s));
                    }
                }
            }
        }

        follow.put(symbol,result);
        return result;
    }
    /*
    public static void main(String args[])
    {
        Map<String,List<String>> grammar=new HashMap<>();

        Scanner sc=new Scanner(System.in);
        System.out.println("Enter grammar rules, finidh using end");

        while(true){
            String stmt=sc.nextLine();
            if(stmt.equals("end")) break;

            String[] parts=stmt.split("->");
            if(parts.length<2) continue;

            grammar.put(parts[0],new ArrayList<>());
            String[] rhs=parts[1].split("\\|");

            for(String i:rhs) grammar.get(parts[0]).add(i);
        }

        Map<String,List<String>> left=removeRecur(grammar);
        printGrammar(left);

        Map<String,List<String>> factored=fact(grammar);
        printGrammar(factored);
    }

    private static void printGrammar(Map<String,List<String>> grammar)
    {
        for(String s:grammar.keySet())
        {
            System.out.println(s+"->"+String.join("|",grammar.get(s)));
        }
    }

    private static Map<String,List<String>> removeRecur(Map<String,List<String>> grammar)
    {
        Map<String,List<String>> newGrammer=new HashMap<>();

        for(String s:grammar.keySet())
        {
            List<String> prod=grammar.get(s);
            List<String> sameStart=new ArrayList<>();
            List<String> not=new ArrayList<>();

            for(String p:prod)
            {
                if(p.startsWith(s))
                {
                    sameStart.add(p.substring(s.length()));
                }
                else not.add(p);
            }

            if(!sameStart.isEmpty()){
                String newT=s+"'";
                List<String> prodMain=new ArrayList<>();
                List<String> follow=new ArrayList<>();

                for(String a:not) prodMain.add(a+newT);
                for(String a:sameStart) follow.add(a+newT);
                follow.add("epsilon");

                newGrammer.put(s,prodMain);
                newGrammer.put(newT,follow);
            }
            else
                newGrammer.put(s,prod);
        }
        return newGrammer;
    }

    private static Map<String,List<String>> fact(Map<String,List<String>> grammer)
    {
        Map<String,List<String>> newGrammer=new LinkedHashMap<>();

        for(String s:grammer.keySet())
        {
            List<String> prod=grammer.get(s);

            String prefix=findCommon(prod);
            if(prefix.isEmpty())
            {
                newGrammer.put(s,prod);
            }
            else{
                List<String> start=new ArrayList<>();
                List<String> follow=new ArrayList<>();
                String newT=s+"'";

                start.add(prefix+newT);
                for(String p:prod)
                {
                    if(p.startsWith(prefix))
                    {
                        String rem=p.substring(prefix.length());
                        follow.add(rem.isEmpty()?"epsilon":rem);
                    }
                    else start.add(p);
                }

                newGrammer.put(s,start);
                newGrammer.put(newT,follow);
            }
        }

        return newGrammer;
    }

    private static String findCommon(List<String> prod)
    {
        if(prod.size()<2) return "";

        String prefix=prod.get(0);
        for(int i=1;i<prod.size();i++)
        {
            String curr=prod.get(i);
            int j=0;

            while(j<prefix.length() && j<curr.length() && prefix.charAt(j)==curr.charAt(j)) j++;
            prefix=prefix.substring(0,j);

            if(prefix.isEmpty()) break;
        }
        return prefix;
    }*/
}
