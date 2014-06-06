package com.microsoft.bingclients.eduapp.utils;

import com.microsoft.bingclients.eduapp.models.SearchItem;
import com.microsoft.bingclients.eduapp.models.SearchSnippet;

public class StringConverter {
	
	private static final String FIRST_LEVEL_SEPARATOR = "---";
	
	private static final String SECOND_LEVEL_SEPARATOR = "===";
	
	private static final String THIRD_LEVEL_SEPARATOR = "=-=";

	public static String convertSearchItemToString(SearchItem item) {
		StringBuilder builder = new StringBuilder();
		builder.append(item.getId())
				.append(FIRST_LEVEL_SEPARATOR)
				.append(item.getTitle())
				.append(FIRST_LEVEL_SEPARATOR)
				.append(item.getDescription())
				.append(FIRST_LEVEL_SEPARATOR)
				.append(item.getImage())
				.append(FIRST_LEVEL_SEPARATOR)
				.append(item.getDuration())
				.append(FIRST_LEVEL_SEPARATOR)
				.append(item.getUrl());
		
		for (int i = 0; i < item.getSnippets().size(); i ++) {
			if (i == 0) {
				builder.append(FIRST_LEVEL_SEPARATOR)
						.append(item.getSnippets().get(i).getSpan())
						.append(THIRD_LEVEL_SEPARATOR)
						.append(item.getSnippets().get(i).getTime());
			} else {
				builder.append(SECOND_LEVEL_SEPARATOR)
						.append(item.getSnippets().get(i).getSpan())
						.append(THIRD_LEVEL_SEPARATOR)
						.append(item.getSnippets().get(i).getTime());
			}
		}
		
		return builder.toString();
	}
	
	public static SearchItem convertStringToSearchItem(String strItem) {
		SearchItem item = new SearchItem();
		String[] firstLevelFields = strItem.split(FIRST_LEVEL_SEPARATOR);
		item.setId(firstLevelFields[0]);
		item.setTitle(firstLevelFields[1]);
		item.setDescription(firstLevelFields[2]);
		item.setImage(firstLevelFields[3]);
		item.setDuration(firstLevelFields[4]);
		item.setUrl(firstLevelFields[5]);
		
		if (firstLevelFields.length > 6) {
			String[] secondLevelFields = firstLevelFields[6].split(SECOND_LEVEL_SEPARATOR);
			
			for (int i = 0; i < secondLevelFields.length; i ++) {
				String[] thirdLevelFields = secondLevelFields[i].split(THIRD_LEVEL_SEPARATOR);
				SearchSnippet snippet = new SearchSnippet();
				snippet.setSpan(thirdLevelFields[0]);
				snippet.setTime(thirdLevelFields[1]);
				item.addSnippet(snippet);
			}
		}
		
		return item;
	}
}
