import React from 'react';
import { useTheme } from 'styled-components';

import Icon from '@common/Icon';
import { $SaleTabBar, $TownSetting, $KeyboardDown } from './SaleTabBar.style';

interface SaleTabBarProps {
  townNames: string;
}

const SaleTabBar = ({ townNames }: SaleTabBarProps) => {
  const theme = useTheme();

  return (
    <$SaleTabBar>
      <$TownSetting>
        <Icon name="townSetting" height={18} width={18} fill={theme.COLORS.NEUTRAL.TEXT.DEFAULT} />
        {townNames}
      </$TownSetting>
      <$KeyboardDown>
        <Icon name="keyboard" height={18} width={18} fill={theme.COLORS.NEUTRAL.TEXT.DEFAULT} />
      </$KeyboardDown>
    </$SaleTabBar>
  );
};

export default SaleTabBar;