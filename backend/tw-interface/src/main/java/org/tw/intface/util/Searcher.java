package org.tw.intface.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Executes the search
 */
public class Searcher {

	/*
	 * The current stack
	 */
	private List<Integer> currentStack;

	public Searcher() {
		this.currentStack = new ArrayList<>();
	}

	public void executeDiffSearch(Pruner pruner, List<List<Integer>> pointsMatrix, int diff, int depth) {
		int currentDiff = diff;
		int currentDepth = depth;
		List<Integer> currentRow = pointsMatrix.get(currentDepth);
		int targetIndex = getTargetIndex(currentRow, currentDiff);
		for (int index = targetIndex; index >= 0; index--) {
			Integer currentValue = currentRow.get(index);
			currentDiff -= currentValue;
			if (currentDiff == 0 && currentDepth == pointsMatrix.size() - 1) {
				currentStack.add(index);
				pruner.addStackToResult(currentStack);
				currentStack.remove(currentStack.size() - 1);
				return;
			}
			if (currentDepth == pointsMatrix.size() - 1) {
				return;
			}
			currentStack.add(index);
			if (pruner.pruneBranch(currentStack)) {
				currentDiff += currentValue;
				currentStack.remove(currentStack.size() - 1);
				continue;
			}
			++currentDepth;
			executeDiffSearch(pruner, pointsMatrix, currentDiff, currentDepth);
			currentStack.remove(currentStack.size() - 1);
			--currentDepth;
			currentDiff += currentValue;
		}
	}

	private int getTargetIndex(List<Integer> list, int target) {
		int index =-1;
		for (Integer integer : list) {
			if (integer <= target) {
				++index;
			}
		}
		return index;
	}

}