class Solution:
    def majorityElement(self, nums):

        ele , cnt = nums[0] , 1

        for i in range(1,len(nums)):

            if nums[i] == ele:
                cnt += 1
            elif cnt>0:
                cnt -= 1
            else:
                ele = nums[i]
                cnt = 1
        
        return ele




            
