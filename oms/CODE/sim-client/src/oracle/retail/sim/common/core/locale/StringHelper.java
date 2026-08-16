package oracle.retail.sim.common.core.locale;

import java.awt.FontMetrics;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.CollationElementIterator;
import java.text.CollationKey;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oracle.retail.sim.common.core.UniversalContext;

public class StringHelper {
  private Locale locale;
  
  private Collator collator;
  
  private Collator ignoreCaseCollator;
  
  public StringHelper(Locale paramLocale) {
    this.locale = paramLocale;
    this.collator = Collator.getInstance(paramLocale);
    this.ignoreCaseCollator = Collator.getInstance(paramLocale);
    this.ignoreCaseCollator.setStrength(1);
  }
  
  public static StringHelper getInstance() {
    return new StringHelper(UniversalContext.getLocale());
  }
  
  public static StringHelper getInstance(Locale paramLocale) {
    return new StringHelper(paramLocale);
  }
  
  public static boolean isNullOrEmpty(String paramString) {
    return (paramString == null || paramString.trim().length() <= 0);
  }
  
  public static String trim(String paramString) {
    return (paramString == null) ? null : paramString.trim();
  }
  
  public static String trimToNull(String paramString) {
    if (paramString == null)
      return null; 
    paramString = paramString.trim();
    return !paramString.isEmpty() ? paramString : null;
  }
  
  public static String trimToEmpty(String paramString) {
    if (paramString == null)
      return ""; 
    paramString = paramString.trim();
    return !paramString.isEmpty() ? paramString : "";
  }
  
  public static String findNotNullOrEmpty(String... paramVarArgs) {
    if (paramVarArgs != null)
      for (String str : paramVarArgs) {
        str = trimToNull(str);
        if (str != null)
          return str; 
      }  
    return null;
  }
  
  public static boolean equalsTrim(String paramString1, String paramString2) {
    return Objects.equals(trimToNull(paramString1), trimToNull(paramString2));
  }
  
  public boolean isEqual(String paramString1, String paramString2) {
    return (paramString1 == null && paramString2 == null) ? true : ((paramString1 == null || paramString2 == null) ? false : this.collator.equals(paramString1, paramString2));
  }
  
  public boolean isEqualTrim(String paramString1, String paramString2) {
    return isEqual(trimToNull(paramString1), trimToNull(paramString2));
  }
  
  public boolean booleanValue(String paramString) {
    if (paramString == null)
      return false; 
    if (isEqual(paramString, "1"))
      return true; 
    String str = paramString.toLowerCase();
    return isEqual(str, Boolean.TRUE.toString().toLowerCase()) ? true : (isEqual(str, "yes"));
  }
  
  public static String booleanToYNString(boolean paramBoolean) {
    return paramBoolean ? "Y" : "N";
  }
  
  public static boolean ynStringToBoolean(String paramString) {
    return "Y".equalsIgnoreCase(paramString);
  }
  
  public int indexOf(String paramString, char paramChar) {
    char[] arrayOfChar = new char[1];
    arrayOfChar[0] = paramChar;
    return indexOf(paramString, new String(arrayOfChar));
  }
  
  public int lastIndexOf(String paramString, char paramChar) {
    char[] arrayOfChar = new char[1];
    arrayOfChar[0] = paramChar;
    return lastIndexOf(paramString, new String(arrayOfChar));
  }
  
  public int indexOf(String paramString1, String paramString2) {
    if (isNullOrEmpty(paramString1) || isNullOrEmpty(paramString2))
      return -1; 
    int[] arrayOfInt = indexRangeOf(paramString1, paramString2);
    return (arrayOfInt[1] == -1) ? -1 : arrayOfInt[0];
  }
  
  public int lastIndexOf(String paramString1, String paramString2) {
    if (isNullOrEmpty(paramString1) || isNullOrEmpty(paramString2))
      return -1; 
    RuleBasedCollator ruleBasedCollator = (RuleBasedCollator)this.collator;
    CollationElementIterator collationElementIterator1 = ruleBasedCollator.getCollationElementIterator(paramString1);
    CollationElementIterator collationElementIterator2 = ruleBasedCollator.getCollationElementIterator(paramString2);
    int k = -1;
    int m = -1;
    int i = collationElementIterator1.next();
    int j = collationElementIterator2.next();
    while (i != -1) {
      if (i == j) {
        if (k == -1)
          k = collationElementIterator1.getOffset(); 
        j = collationElementIterator2.next();
        if (j == -1) {
          m = k;
          collationElementIterator2.reset();
          j = collationElementIterator2.next();
          k = -1;
        } 
      } else if (k != -1) {
        collationElementIterator2.reset();
        j = collationElementIterator2.next();
        k = -1;
      } 
      i = collationElementIterator1.next();
    } 
    if (m > 0)
      m--; 
    return m;
  }
  
  public boolean startsWith(String paramString1, String paramString2) {
    return (indexOf(paramString1, paramString2) == 0);
  }
  
  public int compareTo(String paramString1, String paramString2) {
    return this.collator.compare(paramString1, paramString2);
  }
  
  public int compareToIgnoreCase(String paramString1, String paramString2) {
    return this.ignoreCaseCollator.compare(paramString1, paramString2);
  }
  
  public String[] sort(String[] paramArrayOfString) {
    List<String> list = sort(Arrays.asList(paramArrayOfString));
    return list.<String>toArray(new String[list.size()]);
  }
  
  public List<String> sort(List<String> paramList) {
    ArrayList<CollationKey> arrayList = new ArrayList(paramList.size());
    for (String str : paramList)
      arrayList.add(this.collator.getCollationKey(str)); 
    Collections.sort(arrayList, new StringCollatorComparator());
    ArrayList<String> arrayList1 = new ArrayList(arrayList.size());
    for (CollationKey collationKey : arrayList)
      arrayList1.add(collationKey.getSourceString()); 
    return arrayList1;
  }
  
  public boolean hasMultiplesOfChar(String paramString, char paramChar) {
    return (indexOf(paramString, paramChar) != lastIndexOf(paramString, paramChar));
  }
  
  public String toUpperCase(String paramString) {
    return toUpperCase(paramString, false);
  }
  
  public String toUpperCase(String paramString, boolean paramBoolean) {
    return (paramString != null) ? paramString.toUpperCase(this.locale) : (paramBoolean ? null : "");
  }
  
  public String toLowerCase(String paramString) {
    return toLowerCase(paramString, false);
  }
  
  public String toLowerCase(String paramString, boolean paramBoolean) {
    return (paramString != null) ? paramString.toLowerCase(this.locale) : (paramBoolean ? null : "");
  }
  
  public String substring(String paramString, int paramInt) {
    return paramString.substring(paramInt);
  }
  
  public String substring(String paramString, int paramInt1, int paramInt2) {
    return paramString.substring(paramInt1, paramInt2);
  }
  
  public static String truncate(String paramString, int paramInt) throws UnsupportedEncodingException {
    StringBuilder stringBuilder = new StringBuilder();
    char[] arrayOfChar = paramString.toCharArray();
    int i = 0;
    for (char c : arrayOfChar) {
      i += (String.valueOf(c).getBytes("UTF-8")).length;
      if (i > paramInt)
        break; 
      stringBuilder.append(c);
    } 
    return stringBuilder.toString();
  }
  
  public static String reverseTruncate(String paramString, int paramInt) throws UnsupportedEncodingException {
    String str = (new StringBuilder(paramString)).reverse().toString();
    StringBuilder stringBuilder = new StringBuilder();
    char[] arrayOfChar = str.toCharArray();
    int i = 0;
    for (char c : arrayOfChar) {
      i += (String.valueOf(c).getBytes("UTF-8")).length;
      if (i > paramInt)
        break; 
      stringBuilder.append(c);
    } 
    return stringBuilder.reverse().toString();
  }
  
  public String replace(String paramString1, String paramString2, String paramString3) {
    if (isNullOrEmpty(paramString1))
      return ""; 
    if (isNullOrEmpty(paramString2))
      return paramString1; 
    if (paramString3 == null)
      paramString3 = ""; 
    StringBuilder stringBuilder = new StringBuilder();
    String str = paramString1;
    while (true) {
      int[] arrayOfInt = indexRangeOf(str, paramString2);
      if (arrayOfInt[0] == -1 || arrayOfInt[1] == -1) {
        stringBuilder.append(str);
      } else {
        String str1 = substring(str, 0, arrayOfInt[0]);
        stringBuilder.append(str1);
        stringBuilder.append(paramString3);
        str = substring(str, arrayOfInt[1]);
        continue;
      } 
      return stringBuilder.toString();
    } 
  }
  
  public String getRemainingText(String paramString1, String paramString2) {
    int i = paramString1.lastIndexOf(paramString2);
    if (i == -1)
      return paramString1; 
    if (i + 1 == paramString1.length()) {
      paramString1 = paramString1.substring(0, i);
      i = paramString1.lastIndexOf(paramString2);
      if (i == -1)
        return paramString1; 
    } 
    return paramString1.substring(i + 1);
  }
  
  public String[] splitStringByLength(String paramString, int paramInt) {
    if (isNullOrEmpty(paramString))
      return new String[0]; 
    String[] arrayOfString = paramString.split("\\s", 0);
    StringBuilder stringBuilder = new StringBuilder();
    ArrayList<String> arrayList = new ArrayList();
    for (String str : arrayOfString) {
      if (stringBuilder.length() + str.length() > paramInt) {
        arrayList.add(stringBuilder.toString());
        stringBuilder = new StringBuilder();
      } 
      stringBuilder.append(str);
      stringBuilder.append(" ");
    } 
    arrayList.add(stringBuilder.toString());
    return arrayList.<String>toArray(new String[arrayList.size()]);
  }
  
  public static String removeAllWhitespace(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    char[] arrayOfChar = paramString.toCharArray();
    for (char c : arrayOfChar) {
      if (!Character.isWhitespace(c))
        stringBuilder.append(c); 
    } 
    return stringBuilder.toString();
  }
  
  public static String[] getStringArray(String paramString1, String paramString2) {
    if (isNullOrEmpty(paramString1))
      return new String[0]; 
    if (isNullOrEmpty(paramString2)) {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = paramString1.trim();
      return arrayOfString;
    } 
    return paramString1.split(paramString2, 0);
  }
  
  public int longestSize(FontMetrics paramFontMetrics, String paramString1, String paramString2) {
    String[] arrayOfString = getStringArray(paramString1, paramString2);
    int i = 0;
    if (arrayOfString == null || arrayOfString.length == 0)
      return i; 
    for (String str : arrayOfString) {
      if (str != null) {
        int j = paramFontMetrics.stringWidth(str);
        if (j > i)
          i = j; 
      } 
    } 
    return i;
  }
  
  public static String getMethodName(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString1);
    stringBuilder.append(Character.toUpperCase(paramString2.charAt(0)));
    stringBuilder.append(paramString2.substring(1));
    return stringBuilder.toString();
  }
  
  public static String convertToFilename(String paramString) {
    if (isNullOrEmpty(paramString))
      return ""; 
    String str = File.separator;
    StringBuilder stringBuilder = new StringBuilder();
    char[] arrayOfChar = paramString.toCharArray();
    for (char c : arrayOfChar) {
      if (c == '\\' || c == '/') {
        stringBuilder.append(str);
      } else {
        stringBuilder.append(c);
      } 
    } 
    return stringBuilder.toString();
  }
  
  public String[] removeDuplicateStrings(String[] paramArrayOfString) {
    HashSet hashSet = new HashSet(Arrays.asList((Object[])paramArrayOfString));
    return (String[])hashSet.toArray((Object[])new String[hashSet.size()]);
  }
  
  public int[] indexRangeOf(String paramString1, String paramString2) {
    RuleBasedCollator ruleBasedCollator = (RuleBasedCollator)this.collator;
    CollationElementIterator collationElementIterator1 = ruleBasedCollator.getCollationElementIterator(paramString1);
    CollationElementIterator collationElementIterator2 = ruleBasedCollator.getCollationElementIterator(paramString2);
    int i = collationElementIterator1.next();
    int j = collationElementIterator2.next();
    int k = -1;
    int m = -1;
    while (i != -1) {
      if (i == j) {
        if (k == -1)
          k = collationElementIterator1.getOffset(); 
        j = collationElementIterator2.next();
        if (j == -1) {
          m = collationElementIterator1.getOffset();
          break;
        } 
      } else if (k != -1) {
        collationElementIterator2.reset();
        j = collationElementIterator2.next();
        k = -1;
      } 
      i = collationElementIterator1.next();
    } 
    if (k > 0)
      k--; 
    return new int[] { k, m };
  }
  
  public static String joinToString(Object[] paramArrayOfObject, String paramString) {
    if (paramArrayOfObject == null)
      return "null"; 
    if (paramArrayOfObject.length <= 0)
      return ""; 
    StringBuilder stringBuilder = new StringBuilder();
    boolean bool = false;
    for (Object object : paramArrayOfObject) {
      if (bool) {
        stringBuilder.append(paramString);
      } else if (paramString != null) {
        bool = true;
      } 
      stringBuilder.append(object);
    } 
    return stringBuilder.toString();
  }
  
  public static String joinToString(Collection<?> paramCollection, String paramString) {
    if (paramCollection == null)
      return "null"; 
    if (paramCollection.isEmpty())
      return ""; 
    StringBuilder stringBuilder = new StringBuilder();
    boolean bool = false;
    for (Object object : paramCollection) {
      if (bool) {
        stringBuilder.append(paramString);
      } else if (paramString != null) {
        bool = true;
      } 
      stringBuilder.append(object);
    } 
    return stringBuilder.toString();
  }
  
  public static String joinToStringWithComma(Collection<?> paramCollection) {
    return joinToString(paramCollection, ",");
  }
  
  public static String removeQualifiedPackageNames(String paramString) {
    return paramString.replaceAll("(?:\\w|\\.)+\\.", "");
  }
  
  public boolean containsNumericCharacter(String paramString) {
    Pattern pattern = Pattern.compile("[0-9]");
    Matcher matcher = pattern.matcher(paramString);
    return matcher.find();
  }
  
  public boolean containsLetterCharacter(String paramString) {
    Pattern pattern = Pattern.compile("[a-zA-Z]");
    Matcher matcher = pattern.matcher(paramString);
    return matcher.find();
  }
  
  public boolean containsCapitalCharacter(String paramString) {
    Pattern pattern = Pattern.compile("[A-Z]");
    Matcher matcher = pattern.matcher(paramString);
    return matcher.find();
  }
  
  public boolean containsCharacterFromSet(String paramString1, String paramString2) {
    Pattern pattern = Pattern.compile("[" + paramString2 + "]");
    Matcher matcher = pattern.matcher(paramString1);
    return matcher.find();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\locale\StringHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */