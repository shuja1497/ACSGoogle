/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static  int wordLength = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static HashMap<String,ArrayList> lettersToWord = new HashMap<String, ArrayList>();
    private static ArrayList<String > wordList=new ArrayList<>();
    HashSet<String> wordSet;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            String sorte_word=sortInput(word);
            if(lettersToWord.containsKey(sorte_word))
            {
                lettersToWord.get(sorte_word).add(word);
            }
            else
            {
                ArrayList<String> arr_List=new ArrayList<>();
                arr_List.add(word);

                lettersToWord.put(sorte_word,arr_List);
            }
        }
    }

    public boolean isGoodWord(String word, String base)
    {
        if(base.contains(word))
            return false;
        else
            return true;
    }

    public String sortInput(String s)
    {
        char[] cc=s.toCharArray();
        Arrays.sort(cc);
        return String.valueOf(cc);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0;i<wordList.size();i++)
        {
            String temp1=sortInput(wordList.get(i));
            String temp2=sortInput(targetWord);
            if(temp1.length()==temp2.length()  )
            {
                if(temp1.matches(temp2))
                {
                    result.add(wordList.get(i));
                    System.out.println(i);
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=97;i<=122;i++)
        {
            char a=(char)i;
            String result_new=word+a;
            String sorted_result =sortInput(result_new);
            if(lettersToWord.containsKey(sorted_result))
            {
                for(int j=0;j<(lettersToWord.get(sorted_result)).size();j++)
                {
                    String fetched_word=(String)lettersToWord.get(sorted_result).get(j);
                    if(!word.matches(fetched_word) && !fetched_word.contains(word))
                    {
                        result.add(fetched_word);
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int max=wordList.size()-1;
        int min=0;
        int random_index=min+(int)(Math.random()*((max-min)*1));
        boolean flag=false;
        while(!flag)
        {
            if((wordList.get(random_index).length() == wordLength) && getAnagramsWithOneMoreLetter(wordList.get(random_index)).size()>=MIN_NUM_ANAGRAMS)
            {
                flag=true;
                if(wordLength!=MAX_WORD_LENGTH)
                    wordLength=wordLength+1;
                break;

            }
            random_index=(random_index+1)%wordList.size();
        }

        return wordList.get(random_index);
    }
}
